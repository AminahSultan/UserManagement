# Docs for the Azure Web Apps Deploy action: https://github.com/Azure/webapps-deploy

# More GitHub Actions for Azure: https://github.com/Azure/actions

name: Build and deploy WAR app to Azure Web App - UserManagement-API

on:
push:
branches: - main
workflow_dispatch:

jobs:
build:
runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Set up Java version
        uses: actions/setup-java@v4
        with:
          java-version: 'java21'
          distribution: 'microsoft'

      - name: Build with Maven
        run: mvn clean install

      - name: Upload artifact for deployment job
        uses: actions/upload-artifact@v4
        with:
          name: java-app
          path: '${{ github.workspace }}/target/*.war'

deploy:
runs-on: ubuntu-latest
needs: build
environment:
name: 'Production'
url: ${{ steps.deploy-to-webapp.outputs.webapp-url }}

    steps:
      - name: Download artifact from build job
        uses: actions/download-artifact@v4
        with:
          name: java-app

      - name: Deploy to Azure Web App
        id: deploy-to-webapp
        uses: azure/webapps-deploy@v3
        with:
          app-name: 'UserManagement-API'
          slot-name: 'Production'
          package: '*.war'
          publish-profile: ${{ secrets.AzureAppService_PublishProfile_847e92d326e7403eaace3a27baa2439c }}
