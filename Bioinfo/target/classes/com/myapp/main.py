import funcaoComparacao

f_py      = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/resultado_python.txt")
f_perl    = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/resultado_perl.txt")
f_c       = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/resultado_c.txt")
f_c_plus  = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/resultado_c_plus.txt")
f_c_sharp = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/resultado_c_sharp.txt")
f_java    = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/resultado_java.txt")
f_php     = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW/resultado_php.txt")

arquivo_python = f_py.read()
arquivo_perl = f_perl.read()
arquivo_c_plus = f_c_plus.read()
arquivo_c = f_c.read()
arquivo_c_sharp = f_c_sharp.read()
arquivo_java = f_java.read()
arquivo_php = f_php.read()

arquivos_NW = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/NW"
arquivos_SW = "C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW"

melhorTempoExec = funcaoComparacao.comparacaoTempoExecucao(arquivo_c, arquivo_c_plus, arquivo_c_sharp, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_NW)
melhorScore = funcaoComparacao.comparacaoScore(arquivo_c, arquivo_c_plus, arquivo_c_sharp, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_NW)
menosGaps = funcaoComparacao.comparacaoGap(arquivo_c, arquivo_c_plus, arquivo_c_sharp, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_NW)
menosLinhas = funcaoComparacao.comparacaoLinhasCodigo(arquivo_c, arquivo_c_plus, arquivo_c_sharp, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_NW)

resultado = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/com/myapp/resultado.txt", "w")

print("Comparacoes NW:")
print(f"{melhorTempoExec}")
print(f"{melhorScore}")
print(f"{menosGaps}")
print(f"{menosLinhas}")

# readResultado = open("resultado.txt", "r")
# print(readResultado.read())

f_py.close()
f_perl.close()
f_c_plus.close()
f_c.close()
f_c_sharp.close()
f_java.close()
f_php.close()

resultado.close()
# readResultado.close()

fs_py      = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/resultado_python_SW.txt")
fs_perl    = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/resultado_perl_SW.txt")
fs_c       = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/resultado_c_SW.txt")
fs_c_plus  = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/resultado_c_plus_SW.txt")
fs_c_sharp = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/resultado_c_sharp_SW.txt")
fs_java    = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/resultado_java_SW.txt")
fs_php     = open("C:/Users/Jess/OneDrive/Documentos/NetBeansProjects/BIO/Bioinfo/src/main/java/respostas/SW/resultado_php_SW.txt")

arquivo_python = fs_py.read()
arquivo_perl = fs_perl.read()
arquivo_c_plus = fs_c_plus.read()
arquivo_c = fs_c.read()
arquivo_c_sharp = fs_c_sharp.read()
arquivo_java = fs_java.read()
arquivo_php = fs_php.read()

melhorTempoExec = funcaoComparacao.comparacaoTempoExecucao(arquivo_c_plus, arquivo_c_sharp, arquivo_c, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_SW)
melhorScore = funcaoComparacao.comparacaoScore( arquivo_c_plus, arquivo_c_sharp, arquivo_c, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_SW)
menosGaps = funcaoComparacao.comparacaoGap( arquivo_c_plus, arquivo_c_sharp, arquivo_c, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_SW)
menosLinhas = funcaoComparacao.comparacaoLinhasCodigo( arquivo_c_plus, arquivo_c_sharp, arquivo_c, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_SW)

resultado = open("resultado.txt", "a")

print("Comparacoes SW:")
print(f"{melhorTempoExec}")
print(f"{melhorScore}")
print(f"{menosGaps}")
print(f"{menosLinhas}")

fs_py.close()
fs_perl.close()
fs_c_plus.close()
fs_c.close()
fs_c_sharp.close()
fs_java.close()
fs_php.close()

resultado.close()