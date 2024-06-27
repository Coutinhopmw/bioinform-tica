# Use uma imagem base do Tomcat
FROM tomcat:10.1.24

# Defina o diretório de trabalho dentro do container
WORKDIR /Bioinfo

# Copie o arquivo WAR da aplicação para o diretório webapps do Tomcat
COPY target/Bioinfo-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
COPY ./* /Bioinfo/

# Instale o Python 3 e pip
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
 && rm -rf /var/lib/apt/lists/*

# Instale o PHP
RUN apt-get update && apt-get install -y \
    php \
 && rm -rf /var/lib/apt/lists/*

# Instale o GCC para C++ e C
RUN apt-get update && apt-get install -y \
    build-essential \
 && rm -rf /var/lib/apt/lists/*

# Instale o Perl
RUN apt-get update && apt-get install -y \
    perl \
 && rm -rf /var/lib/apt/lists/*

# Exponha a porta em que o Tomcat estará escutando
EXPOSE 8080

# Comando para iniciar o Tomcat
CMD ["catalina.sh", "run"]
