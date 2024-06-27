# Use uma imagem base do Tomcat
FROM tomcat:10.1.24

# Defina o diretório de trabalho dentro do container
WORKDIR /Bioinfo

# Copie o arquivo WAR da aplicação para o diretório webapps do Tomcat
COPY target/Bioinfo-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
COPY src/ /Bioinfo/src/
COPY database/ /Bioinfo/database/
COPY target/ /Bioinfo/target/

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

# Instale o SDK do .NET para compilar e executar projetos C#
RUN apt-get update && apt-get install -y \
    wget \
    apt-transport-https \
    gnupg \
    && wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.asc.gpg \
    && mv microsoft.asc.gpg /etc/apt/trusted.gpg.d/ \
    && wget -q https://packages.microsoft.com/config/debian/10/prod.list \
    && mv prod.list /etc/apt/sources.list.d/microsoft-prod.list \
    && chown root:root /etc/apt/trusted.gpg.d/microsoft.asc.gpg \
    && chown root:root /etc/apt/sources.list.d/microsoft-prod.list \
    && apt-get update \
    && apt-get install -y dotnet-sdk-6.0 \
    && rm -rf /var/lib/apt/lists/*

# Exponha a porta em que o Tomcat estará escutando
EXPOSE 8080

# Comando para iniciar o Tomcat
CMD ["catalina.sh", "run"]
