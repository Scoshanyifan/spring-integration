# spring-integration-mqtt


demo中消息结构如下：

- AbstractMessage(header, payload)

    - SysMassage
        1. online(connected)
        2. offline(disconnected)
        3. ...
        
    - BizMessage
        1. J00Message(实时上报设备参数等)
        2. J05Message(设备耗气量统计)
        3. JERMessage(设备上报错误信息)
        4. ...


2020.04.10

结合实际项目，将linnei和xiao两种设备连接逻辑整合，方便对比