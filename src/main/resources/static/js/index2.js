let vue = new Vue({
    el: '#app',
    data: {
        value: new Date(),
        loading: false,
        iconUrl: ctx + "assetss/images/favicon.ico",
        iconTitle: "数据库表结构导出工具",
        fit: '',
        form: {
            ip: '',
            port: '',
            dataName: '',
            userName: '',
            password: '',
            dataType: '',
            encode: '',
            tableName: ''
        },
        imageUrl: ctx + "assetss/images/v2/oracle-bg.png",
        name: "Author: xueshuai",
        qq: "QQ: 827681776",
        contentStyle: {
            borderBottom: "1px solid rgb(235, 233, 233)",
            background: "#F2F6FC",
            width: "100%",
            height: "100%"
        },
        dataList: [
            {
                value: 'oracle', label: "Oracle"
            }, {
                value: 'mysql', label: "Mysql"
            }
        ],
        encodeList: [{
            value: "UTF-8", label: "UTF-8"
        }, {
            value: "GBK", label: "GBK"
        }, {
            value: "ISO-8859-1", label: "ISO-8859-1"
        }],
        //表单校验规则
        rules: {
            dataType: {required: true, message: '请选择数据库类型', trigger: 'change'},
            dataName: {required: true, message: '请填写数据库名称', trigger: 'blur'},
            ip: {required: true, message: '请填写IP', trigger: 'blur'},
            port: [
                {required: true, message: '请填写端口号', trigger: 'blur'},
                // {type: 'number', message: '请填写正确端口号', trigger: 'blur'}
            ],
            userName: {required: true, message: '请填写用户名', trigger: 'blur'},
            password: {required: true, message: '请填写密码', trigger: 'blur'},
            encode: {required: true, message: '请选择数据库编码', trigger: 'change'},


        },
    },
    mounted: {},
    methods: {
        generateWord() {
            let formData = this.form;
            // this.validation();
            this.$refs["form"].validate((vaild) => {
                if (vaild) {
                    this.loading = true;
                    axios.post('/getTableList', formData).then((response) => {
                        this.loading = false;
                        //成功
                        if (response.data.code == '200') {
                            //进行markdown导出
                            let markDownMessage = this.getMarkDownMessage(response.data.data);
                            //用marked进行解析markdown消息转为html
                            let htmlcontent = marked(markDownMessage);
                            htmlcontent = "<div id='markdownDiv' class='markdown-body'>" + htmlcontent + "</div>"
                            axios.get("../../css/markdown.css").then(function (data) {
                                let resultMessage = "<html><head><style>" + data.data + "</style></head><body>" + htmlcontent + "</body></html>";
                                let urlObject = window.URL || window.webkitURL || window;
                                let export_blob = new Blob([resultMessage]);
                                let save_link = document.createElementNS("http://www.w3.org/1999/xhtml", "a")
                                save_link.href = urlObject.createObjectURL(export_blob);
                                save_link.download = '数据库表结构文档.doc';
                                let ev = document.createEvent("MouseEvents");
                                ev.initMouseEvent(
                                    "click", true, false, window, 0, 0, 0, 0, 0
                                    , false, false, false, false, 0, null
                                );
                                save_link.dispatchEvent(ev);
                            }).catch(function (error) {
                                console.log(error);
                            });


                        } else {
                            this.$alert(response.data.message, '错误', {
                                confirmButtonText: '确定',
                                type: 'error'
                            });
                        }
                    }).catch((error) => {
                        this.loading = false;
                        this.$alert('网络错误请重试', '错误', {
                            confirmButtonText: '确定',
                            type: 'error'
                        });
                    });
                } else {
                    return false;
                }
            });

        },

        getMarkDownMessage(data) {
            let resultLength = data.length;
            let resultMessage = '';
            for (let i = 0; i < resultLength; i++) {
                let tableInfo = data[i];
                let index = i + 1;
                let tableName = this.replaceUnderline(tableInfo.tableName);
                let tableChName = this.replaceUnderline(tableInfo.tableNameCn);
                let message = '### ' + index + '.' + tableChName + "(" + tableName + ")\n" +
                    // '#### ' + index + '.1 字段说明\n' +
                    // '\n' +
                    '|  字段名  |  字段中文名  |  类型  |  长度  |  默认值  |  可为空  | \n' +
                    '| ------| ------| ------| ------| ------| ------|\n';
                let columnList = tableInfo.columnEntity;
                let columnLength = columnList.length;
                let columnMessage = '';
                for (let j = 0; j < columnLength; j++) {
                    let columnInfo = columnList[j];
                    let columnName = this.replaceUnderline(columnInfo.columnName);
                    let columnChName = this.replaceUnderline(columnInfo.columnNameCn);
                    let defaultData = this.replaceUnderline(columnInfo.defaultData);
                    let isNotNull = this.replaceUnderline(columnInfo.nullAble);
                    let dataType = this.replaceUnderline(columnInfo.dataType);
                    let dataLength = this.replaceUnderline(columnInfo.dataLength + '');
                    columnMessage = columnMessage + "|" + columnName + "|" + columnChName + "|" + dataType + "|" + dataLength + "|" + defaultData + "|" + isNotNull + "| \n";
                }
                resultMessage += (message + columnMessage);
            }
            return resultMessage;
        },
        replaceUnderline(str) {
            return (str == null || str == undefined) ? '' : str.replace(new RegExp('_', 'g'), '\\_');
        },
        exportRaw(name, data) {
            let urlObject = window.URL || window.webkitURL || window;
            let export_blob = new Blob([data]);
            let save_link = document.createElementNS("http://www.w3.org/1999/xhtml", "a")
            save_link.href = urlObject.createObjectURL(export_blob);
            save_link.download = name;
            this.fake_click(save_link);
        },
        //模拟操作
        fake_click(obj) {
            let ev = document.createEvent("MouseEvents");
            ev.initMouseEvent(
                "click", true, false, window, 0, 0, 0, 0, 0
                , false, false, false, false, 0, null
            );
            obj.dispatchEvent(ev);
        },
        //数据库类型下拉点击事件
        changeDataType(val) {
            this.dataType = val;
            if (val == "oracle") {
                this.imageUrl = ctx + "assetss/images/v2/oracle-bg.png"
            } else if (val == "mysql") {
                this.imageUrl = ctx + "assetss/images/v2/mysql-bg.png"
            }

        },
        /*数据库连接测试*/
        testConnection() {
            let formData = this.form;
            this.$refs["form"].validate((valid)=>{
                if(valid){
                    axios.post('/testConnection', formData).then((response) => {
                        if (response.data.code == '200') {
                            if (response.data.message) {
                                this.$alert('数据库连接成功', '成功', {
                                    confirmButtonText: '确定', type: 'success'
                                })
                            } else {
                                this.$alert('数据库连接失败，请检查数据库信息！', '失败', {
                                    confirmButtonText: '确定', type: 'error'
                                })
                            }
                        } else {
                            this.$alert(response.data.message, '错误', {
                                confirmButtonText: '确定',
                                type: 'error'
                            });
                        }
                    }).catch((error) => {
                        this.$alert('数据库连接失败，请检查数据库信息！', '失败', {
                            confirmButtonText: '确定', type: 'error'
                        })
                    });
                }else{
                    return false;
                }
            });

        }
    }
});