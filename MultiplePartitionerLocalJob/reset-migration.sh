CONTAINER='mysqlsrv'
docker exec $CONTAINER mysql -u root --password='123456' -D migracao_dados --execute="DELETE from pessoa;
                                                                                      DELETE from dados_bancarios;"