import xml.etree.ElementTree as ET
import matplotlib.pyplot as plt

# Função para ler o arquivo XML e extrair os dados
def ler_arquivo_xml(nome_arquivo):
    tree = ET.parse(nome_arquivo)
    root = tree.getroot()
    
    bit_scores = []
    
    for hit in root.findall('.//Hit_hsps/Hsp'):
        bit_score = float(hit.find('Hsp_bit-score').text)
        bit_scores.append(bit_score)
    
    return bit_scores

# Função para gerar o gráfico de barras
def gerar_grafico_barras(bit_scores):
    plt.figure(figsize=(10, 6))
    plt.bar(range(len(bit_scores)), bit_scores, color='blue')
    plt.xlabel('HSP Número')
    plt.ylabel('Bit Score')
    plt.title('Gráfico de Barras de Bit Scores de HSPs')
    plt.show()

# Nome do arquivo XML
nome_arquivo = 'resultado.xml'

# Ler o arquivo XML
bit_scores = ler_arquivo_xml(nome_arquivo)

# Gerar o gráfico de barras
gerar_grafico_barras(bit_scores)