# HomeWork
①代码在master分支
②第一题可以直接postman测试
③第二题的.csv数据已经转换为.xlsx导入了数据库
导入接口  http://localhost:10001/excel   POST
如果想测试该接口，先调用 http://localhost:10001/delete  DELETE，删除目前数据库的内容
postman测试： 在body选择form-data，key为file，类型也选择file，value选择 xlsx文件

获得列表接口：http://localhost:10001/fund?pageNum=1&pageSize=5&sortField=1&sortDirection=1
sortField 的值为0-8，分别对应单位净值、近一周、近一月、近三月、近一年、近两年、近三年、今年来、成立来
sortDirection的值为0-1，0为升序，1位降序
代码固定在计算9月8号的数据，如果想更改当天，将GrowthServiceImpl的getFundGrowthPage的第一行代码取消注释，
将setGrowth方法的“9月8号”相关代码注释，将“正常情况”取消注释。

平常从数据库获取，每天凌晨、下午4点到6点每隔5分钟重新计算并更新数据库
