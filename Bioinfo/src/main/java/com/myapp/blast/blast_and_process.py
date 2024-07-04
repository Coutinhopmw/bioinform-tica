import requests
import time
import sys
import xml.etree.ElementTree as ET
import json

# Endpoint do NCBI BLAST
NCBI_BLAST_URL = "https://blast.ncbi.nlm.nih.gov/Blast.cgi"

# Função para converter XML em um dicionário
def xml_to_dict(element):
    node = {}
    if element.attrib:
        node.update(element.attrib)
    if element.text and element.text.strip():
        node['text'] = element.text.strip()
    for child in element:
        child_name = child.tag
        child_dict = xml_to_dict(child)
        if child_name not in node:
            node[child_name] = child_dict
        else:
            if not isinstance(node[child_name], list):
                node[child_name] = [node[child_name]]
            node[child_name].append(child_dict)
    return node

# Função para fazer a consulta BLAST e obter os resultados como string XML
def run_blast(sequence):
    params = {
        'CMD': 'Put',
        'PROGRAM': 'blastn',
        'DATABASE': 'nt',
        'QUERY': sequence
    }

    response = requests.post(NCBI_BLAST_URL, data=params)
    response.raise_for_status()

    rid = None
    for line in response.text.splitlines():
        if 'RID =' in line:
            rid = line.split('=')[1].strip()
            break

    if rid:
        status = 'WAITING'
        while status == 'WAITING':
            time.sleep(10)
            status_response = requests.get(NCBI_BLAST_URL, params={
                'CMD': 'Get',
                'RID': rid,
                'FORMAT_OBJECT': 'SearchInfo'
            })
            status_response.raise_for_status()

            for line in status_response.text.splitlines():
                if 'Status=' in line:
                    status = line.split('=')[1].strip()
                    break

        if status == 'READY':
            result_response = requests.get(NCBI_BLAST_URL, params={
                'CMD': 'Get',
                'RID': rid,
                'FORMAT_TYPE': 'XML'
            })
            result_response.raise_for_status()
            return result_response.text
        else:
            print(f"Status final da consulta: {status}")
            return None
    else:
        print("Erro ao obter o RID")
        return None

# Função para converter XML string para dicionário JSON
def convert_xml_to_json(xml_string):
    root = ET.fromstring(xml_string)
    xml_dict = xml_to_dict(root)
    json_data = json.dumps(xml_dict, indent=4)
    return json_data

# Função para processar os hits do JSON
def process_hits(json_string, num_hits=100):
    data = json.loads(json_string)
    hits = data['BlastOutput_iterations']['Iteration']['Iteration_hits']['Hit']
    for i in range(min(num_hits, len(hits))):
        hit = hits[i]
        sequence_name = hit.get('Hit_def', {}).get('text', 'N/A')
        score = hit.get('Hit_hsps', {}).get('Hsp', {}).get('Hsp_score', {}).get('text', 'N/A')
        e_value = hit.get('Hit_hsps', {}).get('Hsp', {}).get('Hsp_evalue', {}).get('text', 'N/A')
        identity = hit.get('Hit_hsps', {}).get('Hsp', {}).get('Hsp_identity', {}).get('text', 'N/A')

        seq1 = hit.get('Hit_hsps', {}).get('Hsp', {}).get('Hsp_qseq', {}).get('text', 'N/A')
        aling = hit.get('Hit_hsps', {}).get('Hsp', {}).get('Hsp_midline', {}).get('text', 'N/A')
        seq2 = hit.get('Hit_hsps', {}).get('Hsp', {}).get('Hsp_hseq', {}).get('text', 'N/A')

        sequence_length = hit.get('Hit_len', {}).get('text', 'N/A')
        aling_length = hit.get('Hit_hsps', {}).get('Hsp', {}).get('Hsp_align-len', {}).get('text', 'N/A')

        print("Alinhamento:")
        print(seq1)
        print(aling)
        print(seq2)
        print(f"Nome da sequencia: {sequence_name}")
        print(f"Score: {score}")
        print(f"Identity: {identity}")
        print(f"E-value: {e_value}")
        print(f"Tamanho da sequencia: {sequence_length}")
        print(f"Tamanho do alinhamento: {aling_length}","\n")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        # print("Uso: python blast_and_process.py <sequência>")
        sys.exit(1)
    
    sequence = sys.argv[1]
    xml_string = run_blast(sequence)
    
    if xml_string:
        json_string = convert_xml_to_json(xml_string)
        process_hits(json_string)
