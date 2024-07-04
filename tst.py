import requests
import time
import sys

# Endpoint do NCBI BLAST
NCBI_BLAST_URL = "https://blast.ncbi.nlm.nih.gov/Blast.cgi"

sequencia = sys.argv[1]

# Dados da consulta BLAST
params = {
    'CMD': 'Put',
    'PROGRAM': 'blastn',  # ou blastp, blastx, etc.
    'DATABASE': 'nt',     # banco de dados a ser utilizado
    'QUERY': sequencia
    # sequência de exemplo
}

# Enviar a consulta
response = requests.post(NCBI_BLAST_URL, data=params)
if response.status_code == 200:
    rid = None
    for line in response.text.splitlines():
        if 'RID =' in line:
            rid = line.split('=')[1].strip()
            break

    if rid:
        print(f'Request ID (RID): {rid}')
        
        # Aguardar até que a consulta esteja pronta
        status = 'WAITING'
        while status == 'WAITING':
            time.sleep(1)  # Aguardar 10 segundos entre verificações
            status_response = requests.get(NCBI_BLAST_URL, params={
                'CMD': 'Get',
                'RID': rid,
                'FORMAT_OBJECT': 'SearchInfo'
            })
            if status_response.status_code == 200:
                for line in status_response.text.splitlines():
                    if 'Status=' in line:
                        status = line.split('=')[1].strip()
                        break
            else:
                status = 'ERROR'

        if status == 'READY':
            # Recuperar os resultados
            result_response = requests.get(NCBI_BLAST_URL, params={
                'CMD': 'Get',
                'RID': rid,
                'FORMAT_TYPE': 'XML'  # ou 'HTML', 'Text'
            })
            if result_response.status_code == 200:
                resultado = open("resultado.xml", "w")
                resultado.write(result_response.text)
                print(result_response.text)
            else:
                print(f"Erro ao recuperar os resultados: {result_response.status_code}")
        else:
            print(f"Status final da consulta: {status}")
    else:
        print("Erro ao obter o RID")
else:
    print(f"Erro ao enviar a consulta: {response.status_code}")
