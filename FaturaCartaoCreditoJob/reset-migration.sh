CONTAINER='mysqlsrv'
docker exec $CONTAINER mysql -u root --password='123456' -D fatura_cartao_credito --execute="DELETE from cartao_credito;
                                                                                      DELETE from transacao;
                                                                                      DELETE from fatura;"