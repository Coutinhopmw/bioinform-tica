import xml.etree.ElementTree as ET
import json

# Função para converter XML em um dicionário
def xml_to_dict(element):
    node = {}
    # Se o elemento tem atributos, adiciona-os ao dicionário
    if element.attrib:
        node.update(element.attrib)
    # Se o elemento tem texto, adiciona ao dicionário
    if element.text and element.text.strip():
        node['text'] = element.text.strip()
    # Adiciona os filhos do elemento ao dicionário
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

# Ler o arquivo XML
tree = ET.parse('resultado.xml')
root = tree.getroot()

# Converter o XML para dicionário
xml_dict = xml_to_dict(root)

# Converter o dicionário para JSON
json_data = json.dumps(xml_dict, indent=4)

# Salvar o JSON em um arquivo
with open('arquivo.json', 'w') as json_file:
    json_file.write(json_data)

print("Conversão concluída. JSON salvo em 'arquivo.json'.")

with open('arquivo.json', 'r') as file:
    json_string = file.read()

try:
    data = json.loads(json_string)
except json.JSONDecodeError as e:
    print(f"Erro ao decodificar JSON: {e}")
    exit(1)

# Acessar os elementos específicos
i = 0
while i <= 99:
    hit = data['BlastOutput_iterations']['Iteration']['Iteration_hits']['Hit'][i]
    sequence_name = hit['Hit_def']['text']
    score = hit['Hit_hsps']['Hsp']['Hsp_score']['text']
    e_value = hit['Hit_hsps']['Hsp']['Hsp_evalue']['text']
    sequence_length = hit['Hit_len']['text']
    
    

    print(f"Nome da sequência: {sequence_name}")
    print(f"Score: {score}")
    print(f"E-value: {e_value}")
    print(f"Tamanho da sequência: {sequence_length}")

    i += 1