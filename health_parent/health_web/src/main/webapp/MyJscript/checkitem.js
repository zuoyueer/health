/*!
 * 对应 page/checkitem.html 的js文件
 * ---------------------------------
 * 主要是对检查项的增删改查
 */
var vue = new Vue({
    el: '#app',
    data: {
        pagination: {//分页相关模型数据
            currentPage: 1,//当前页码
            pageSize: 10,//每页显示的记录数
            total: 0,//总记录数
            queryString: null//查询条件
        },
        dataList: [],//当前页要展示的分页列表数据
        formData: {},//表单数据
        dialogFormVisible: false,//增加表单是否可见
        dialogFormVisible4Edit: false,//编辑表单是否可见
        rules: {//校验规则
            code: [{required: true, message: '项目编码为必填项', trigger: 'blur'}],
            name: [{required: true, message: '项目名称为必填项', trigger: 'blur'}]
        }
    },
    //钩子函数，VUE对象初始化完成后自动执行
    created() {
        //页面一加载就分页查询
        this.findPage();
    },
    methods: {
        //编辑
        handleEdit() {
            //校验通过发送异步请求修改
            this.$refs['dataEditForm'].validate(valid => {
                if (valid) {
                    //将表单编辑框隐藏
                    this.dialogFormVisible4Edit = false;
                    //发送请求
                    axios.post('/checkitem/edit.do', this.formData).then(response => {
                        //刷新页面
                        this.findPage();
                        //成功或者失败给提示
                        this.$message({
                            message: response.data.message,
                            type: response.data.flag === true ? "success" : "error"
                        });
                    });
                } else {
                    //表单校验失败
                    this.$message.error("表单数据校验失败");
                    return false;
                }
            });
        },
        //添加
        handleAdd() {
            //validate:提交表单前再次校验
            this.$refs['dataAddForm'].validate((valid) => {
                //如果表单校验成功
                if (valid) {
                    //发送ajax请求将表单的数据提交到后台
                    axios.post("/checkitem/add.do", this.formData).then(response => {
                        //隐藏新增窗口
                        this.dialogFormVisible = false;
                        //判断后台返回的falg值,true表示添加操作成功,false表示添加失败
                        if (response.data.flag) {
                            this.$message({
                                message: response.data().message,
                                type: 'success'
                            });
                        } else {
                            this.$message.error(response.data.message);
                        }
                    }).finally(() => {
                        this.findPage();
                    });
                } else {
                    this.$message.error("表单数据校验失败");
                    return false;
                }
            });
        },
        //分页查询
        findPage() {
            var param = {
                //当前页码
                currentPage: this.pagination.currentPage,
                //每页显示的记录数
                pageSize: this.pagination.pageSize,
                //查询条件
                queryString: this.pagination.queryString
            };
            //发送分页查询异步请求
            axios.post("/checkitem/findPage.do", param).then(response => {
                //为模型数据赋值,分别是分页的数据和总页数
                this.dataList = response.data.rows;
                this.pagination.total = response.data.total;
            });
        },
        //  条件查询,解决不在首页无法查询到结果的bug
        findPageByCondition() {
            this.pagination.currentPage = 1;
            this.findPage();
        },
        // 重置表单
        resetForm() {
            this.formData = {};
        },
        // 弹出添加窗口
        handleCreate() {
            //每次点击新建按钮,清空表单输入项
            this.resetForm();
            this.dialogFormVisible = true;
        },
        // 弹出编辑窗口
        handleUpdate(row) {
            this.dialogFormVisible4Edit = true;
            axios.get("/checkitem/findById.do?id=" + row.id).then(response => {
                this.$message({
                    message: response.data.message,
                    type: response.data.flag === true ? 'success' : 'error'
                });
                this.formData = response.data.data;
            });
            //不能使用this.formData = row;来回显数据,否则会出现数据不是最新的,取消修改模型的数据也会生效的问题
        },
        //切换页码,这是分页组件执行的方法,分页组件中有设置
        handleCurrentChange(currentPage) {
            //currentPage是切换后的页码
            this.pagination.currentPage = currentPage;
            this.findPage();
        },
        // 删除
        handleDelete(row) {
            //防止误删除..二次确认
            this.$confirm("确认删除当前选中记录吗?", "提示", {type: "warning"}).then(() => {
                //如果用户确认删除 则发送删除异步请求
                axios.get("/checkitem/delete.do?id=" + row.id).then(response => {
                    if (response.data.flag) {
                        //删除成功
                        this.$message({
                            message: response.data.message,
                            type: 'success'
                        });
                        //调用分页,获取最新分页数据
                        this.findPage();
                    } else {
                        this.$message.error(response.data.message);
                    }
                }).catch(r => {
                    this.showErrorMessage(r);
                });
            }).catch(()=>{

            });
        },
        showErrorMessage(r){
            if (r=='Error: Request failed with status code 403'){
                this.$message.error("权限不足");
            }else {
                this.$message.error('未知错误')
            }
            console.log(r)
        }
    }
});
