import funçãoComparacao

f_py = open("arquivos/arquivos_NW/resultado_python.txt")
f_perl = open("arquivos/arquivos_NW/resultado_perl.txt")
f_c = open("arquivos/arquivos_NW/resultado_c.txt")
f_c_plus = open("arquivos/arquivos_NW/resultado_c_plus.txt")
f_c_sharp = open("arquivos/arquivos_NW/resultado_c_sharp.txt")
f_java = open("arquivos/arquivos_NW/resultado_java.txt")
f_php = open("arquivos/arquivos_NW/resultado_php.txt")

arquivo_python = f_py.read()
arquivo_perl = f_perl.read()
arquivo_c_plus = f_c_plus.read()
arquivo_c = f_c.read()
arquivo_c_sharp = f_c_sharp.read()
arquivo_java = f_java.read()
arquivo_php = f_php.read()

arquivos_NW = "arquivos/arquivos_NW"
arquivos_SW = "arquivos/arquivos_SW"

melhorTempoExec = funçãoComparacao.comparacaoTempoExecucao(arquivo_c, arquivo_c_plus, arquivo_c_sharp, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_NW)
melhorScore = funçãoComparacao.comparacaoScore(arquivo_c, arquivo_c_plus, arquivo_c_sharp, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_NW)
menosGaps = funçãoComparacao.comparacaoGap(arquivo_c, arquivo_c_plus, arquivo_c_sharp, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_NW)
menosLinhas = funçãoComparacao.comparacaoLinhasCodigo(arquivo_c, arquivo_c_plus, arquivo_c_sharp, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_NW)

resultado = open("resultado.txt", "w")

resultado.write("Comparações NW: \n")
resultado.write(f"{melhorTempoExec}\n")
resultado.write(f"{melhorScore}\n")
resultado.write(f"{menosGaps}\n")
resultado.write(f"{menosLinhas}\n")

# readResultado = open("resultado.txt", "r")
# print(readResultado.read())

resultado.close()
# readResultado.close()

f_py = open("arquivos/arquivos_SW/resultado_python_SW.txt")
f_perl = open("arquivos/arquivos_SW/resultado_perl_SW.txt")
f_c = open("arquivos/arquivos_SW/resultado_c_SW.txt")
f_c_plus = open("arquivos/arquivos_SW/resultado_c_plus_SW.txt")
f_c_sharp = open("arquivos/arquivos_SW/resultado_c_sharp_SW.txt")
f_java = open("arquivos/arquivos_SW/resultado_java_SW.txt")
f_php = open("arquivos/arquivos_SW/resultado_php_SW.txt")

arquivo_python = f_py.read()
arquivo_perl = f_perl.read()
arquivo_c_plus = f_c_plus.read()
arquivo_c = f_c.read()
arquivo_c_sharp = f_c_sharp.read()
arquivo_java = f_java.read()
arquivo_php = f_php.read()

melhorTempoExec = funçãoComparacao.comparacaoTempoExecucao(arquivo_c_plus, arquivo_c_sharp, arquivo_c, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_SW)
melhorScore = funçãoComparacao.comparacaoScore( arquivo_c_plus, arquivo_c_sharp, arquivo_c, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_SW)
menosGaps = funçãoComparacao.comparacaoGap( arquivo_c_plus, arquivo_c_sharp, arquivo_c, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_SW)
menosLinhas = funçãoComparacao.comparacaoLinhasCodigo( arquivo_c_plus, arquivo_c_sharp, arquivo_c, arquivo_java,  arquivo_perl, arquivo_php, arquivo_python, arquivos_SW)

resultado = open("resultado.txt", "a")

resultado.write("Comparações SW: \n")
resultado.write(f"{melhorTempoExec}\n")
resultado.write(f"{melhorScore}\n")
resultado.write(f"{menosGaps}\n")
resultado.write(f"{menosLinhas}\n")