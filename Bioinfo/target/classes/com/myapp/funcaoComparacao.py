import os

def comparacaoTempoExecucao(resultado_c, resultado_c_plus, resultado_c_sharp, resultado_java,  resultado_perl, resultado_php, resultado_python, arquivo):
    
    linguagens = [
        resultado_c, resultado_c_plus, resultado_c_sharp, resultado_java,  resultado_perl, resultado_php, resultado_python
    ]

    nomesLinguagens = os.listdir(arquivo)

    i = 0
    melhorLinguagemTempoExec = None
    melhorTempoExecucao = float('inf')
    for i, x in enumerate(linguagens):
        try:
            listaValoresLinguagens = x.rsplit("\n")
            tempoExec = float(listaValoresLinguagens[1])
            if tempoExec < melhorTempoExecucao:
                melhorTempoExecucao = tempoExec
                melhorLinguagemTempoExec = nomesLinguagens[i]
        except:
            continue
        i += 1

    return melhorLinguagemTempoExec, melhorTempoExecucao


def comparacaoScore(resultado_c_plus, resultado_c_sharp, resultado_c, resultado_java,  resultado_perl, resultado_php, resultado_python, arquivo):
    
    linguagens = [
        resultado_c_plus, resultado_c_sharp, resultado_c, resultado_java,  resultado_perl, resultado_php, resultado_python
    ]

    nomesLinguagens = os.listdir(arquivo)
    nomesLinguagens.sort()

    i = 0
    melhorLinguagemScore = None
    melhorScore = float('-inf')
    for i, x in enumerate(linguagens):
        try:
            listaValoresLinguagens = x.rsplit("\n")
            score = int(listaValoresLinguagens[2])
            if score > melhorScore:
                melhorScore = score
                melhorLinguagemScore = nomesLinguagens[i]
        except:
            continue
        i += 1

    return melhorLinguagemScore, melhorScore


def comparacaoGap(resultado_c_plus, resultado_c_sharp, resultado_c, resultado_java,  resultado_perl, resultado_php, resultado_python, arquivo):
    
    linguagens = [
        resultado_c_plus, resultado_c_sharp, resultado_c, resultado_java,  resultado_perl, resultado_php, resultado_python
    ]

    nomesLinguagens = os.listdir(arquivo)

    i = 0
    melhorLinguagemGaps = None
    menorGap = float('inf')
    for i, x in enumerate(linguagens):
        try:
            listaValoresLinguagens = x.rsplit("\n")
            gaps = int(listaValoresLinguagens[3])
            if gaps < menorGap:
                menorGap = gaps
                melhorLinguagemGaps = nomesLinguagens[i]
        except:
            continue
        i += 1

    return melhorLinguagemGaps, menorGap

def comparacaoLinhasCodigo(resultado_c_plus, resultado_c_sharp, resultado_c, resultado_java,  resultado_perl, resultado_php, resultado_python, arquivo):
    
    linguagens = [
        resultado_c_plus, resultado_c_sharp, resultado_c, resultado_java,  resultado_perl, resultado_php, resultado_python
    ]

    nomesLinguagens = os.listdir(arquivo)

    i = 0
    melhorLinguagemQtdLinhas = None
    menorQtdLinhas = float('inf')
    for i, x in enumerate(linguagens):
        try:
            listaValoresLinguagens = x.rsplit("\n")
            qtdLinhas = int(listaValoresLinguagens[4])
            if qtdLinhas < menorQtdLinhas:
                menorQtdLinhas = qtdLinhas
                melhorLinguagemQtdLinhas = nomesLinguagens[i]
        except:
            continue
        i += 1

    return melhorLinguagemQtdLinhas, menorQtdLinhas
    