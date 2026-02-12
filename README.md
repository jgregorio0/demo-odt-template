# demo-odt-template

```shell
curl -v http://localhost:8080/resources/test
```

```shell
curl -v -X POST http://localhost:8080/resources/pdf ^
     -H "Content-Type: application/json" ^
     -d @post-resources-pdf.json ^
     -o test.pdf
```

```shell
curl -v -X POST http://localhost:8080/resources/zip ^
     -H "Content-Type: application/json" ^
     -d @post-resources-zip.json ^
     -o test.zip
```
