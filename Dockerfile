# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
#FROM openjdk:8-alpine
#FROM jitesoft/alpine
FROM openjdk:11
ADD target/FacturaEnLinea-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9011
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["java", "Main"]

