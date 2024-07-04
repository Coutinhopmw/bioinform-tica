# Use uma imagem base do Tomcat
FROM tomcat:10.1.24

# Defina o diretório de trabalho dentro do container
WORKDIR /Bioinfo

# Copie o arquivo WAR da aplicação para o diretório webapps do Tomcat
COPY target/Bioinfo-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
COPY src/ /Bioinfo/src/
COPY target/ /Bioinfo/target/

# Instale dependências básicas
RUN apt-get update && apt-get install -y \
    wget \
    apt-transport-https \
    gnupg \
    software-properties-common

# Adicione a chave GPG e o repositório do .NET SDK
RUN wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > /etc/apt/trusted.gpg.d/microsoft.asc.gpg \
    && wget -q https://packages.microsoft.com/config/debian/10/prod.list -O /etc/apt/sources.list.d/microsoft-prod.list

# Instale o .NET SDK
RUN apt-get update \
    && apt-get install -y dotnet-sdk-6.0 \
    && rm -rf /var/lib/apt/lists/*

# Instale o SDK Mono
RUN apt-get update && apt-get install -y \
    mono-devel \
 && rm -rf /var/lib/apt/lists/*

# Instale Python 3 e pip
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

# Configurar variáveis de ambiente para cada linguagem
ENV PYTHON_HOME=/usr/bin/python3 \
    PYTHONPATH=/usr/lib/python3.7 \
    PHP_HOME=/usr/bin/php \
    GCC_HOME=/usr/bin/gcc \
    GPP_HOME=/usr/bin/g++ \
    PERL_HOME=/usr/bin/perl \
    DOTNET_ROOT=/usr/share/dotnet \
    MONO_HOME=/usr/bin/mono \
    PATH="$PYTHON_HOME:$PHP_HOME:$GCC_HOME:$GPP_HOME:$PERL_HOME:$DOTNET_ROOT:$MONO_HOME:$PATH"
    
RUN pip install requests
# Exponha a porta em que o Tomcat estará escutando
EXPOSE 8080

# Comando para iniciar o Tomcat
CMD ["catalina.sh", "run"]
