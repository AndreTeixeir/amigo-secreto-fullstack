# Estágio 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar pom.xml e baixar dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fonte e compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar JAR do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Criar diretório para wallet
RUN mkdir -p /app/wallet

# Expor porta
EXPOSE 8080

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod
ENV TNS_ADMIN=/app/wallet

# Executar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

