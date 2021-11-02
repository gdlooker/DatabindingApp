package com.example.dell.databindingapp.bean

data class KickOutInfo(var userId: String?,
                       var deviceType: Int,
                       var ip: String?,
                       var port: Int,
                       var sub_type:Int?, //类型，3是管理后台强制下线
                       var msg_title:String?)  //原因