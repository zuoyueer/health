/*!
 * 对应 page/user.html 的js文件
 * ---------------------------------
 * 主要是对用户的增删改查
 */
var vue = new Vue({
    el: '#app',
    data: {
        activeName: 'first',//添加/编辑窗口Tab标签名称
        pagination: {//分页相关属性
            currentPage: 1,
            pageSize: 2,
            total: 100,
            queryString: null,
        },
        dataList: [],//列表数据
        formData: {},//表单数据
        tableData: [],//新增和编辑表单中对应的检查项列表数据
        roleIds: [],//新增和编辑表单中检查项对应的复选框，基于双向绑定可以进行回显和数据提交
        dialogFormVisible: false,//控制添加窗口显示/隐藏
        dialogFormVisible4Edit: false//控制编辑窗口显示/隐藏
    },
    created() {
        this.findPage();
    },
    methods: {
        //编辑
        handleEdit() {
            //发请求,更新数据
            axios.post("/user/edit.do?roleIds=" + this.roleIds, this.formData).then(response => {
                //隐藏编辑窗口
                this.dialogFormVisible4Edit = false;
                //提示信息
                this.$message({
                    message: response.data.message,
                    type: response.data.flag === true ? 'success' : 'error'
                });
            }).finally(() => {
                //刷新列表
                this.findPage();
            });
        },
        //添加
        handleAdd() {
            //发送请求
            axios.post("/user/add.do?roleIds=" + this.roleIds, this.formData).then(response => {
                //关闭新增窗口
                this.dialogFormVisible = false;
                this.$message({
                    message: response.data.message,
                    type: response.data.flag === true ? 'success' : 'error'
                });
            }).finally(() => {
                this.findPage();
            });
        },
        //分页查询
        findPage() {
            //分页查询数据
            var param = {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                queryString: this.pagination.queryString
            };
            axios.post("/user/findPage.do", param).then(response => {
                this.dataList = response.data.rows;
                this.pagination.total = response.data.total;
            });
        },
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
            //每次点击添加,都清空上次的数据
            this.resetForm();
            //弹出添加窗口
            this.dialogFormVisible = true;
            //默认切换到第一个标签页
            this.activeName = 'first';
            //重置
            this.roleIds = [];
            //发送ajax请求查询下全部检查项信息
            axios.get("/role/findAll.do").then(response => {
                if (response.data.flag) {
                    //将检查项列表数据赋值给模型数据, 用于页面表格显示
                    this.tableData = response.data.data;
                } else {
                    this.$message.error(response.data.message);
                }
            });
        },
        // 弹出编辑窗口
        handleUpdate(row) {
            //弹出窗口
            this.dialogFormVisible4Edit = true;
            //发请求查询
            axios.get("/user/findById.do?id=" + row.id).then(response => {
                //查询成功
                if (response.data.flag) {
                    //默认选中第一个标签页
                    this.activeName = 'first';
                    //为模型赋值
                    this.formData = response.data.data;
                    //发送请求查询全部的检查项信息,用于检查项表格显示
                    axios.get("/role/findAll.do").then(response => {
                        if (response.data.flag) {
                            //为模型赋值
                            this.tableData = response.data.data;
                            //查询当前检查组包含的检查项的id集合
                            axios.get("/user/findRoleIdsByUserId.do?id=" + row.id).then(response => {
                                //注意,这里只要一个 response.data;就可以了,别多写了
                                this.roleIds = response.data;
                            });
                        } else {
                            this.$message.error(response.data.message);
                        }
                    });
                } else {
                    this.$message.error("获取数据失败,请刷新当前页面");
                }
            });
        },
        //切换页码
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.findPage();
        },
        // 删除
        handleDelete(row) {
            //防止误删,二次确认
            this.$confirm("确定要删除选择的检查组吗?", "提示", {type: "warning"}).then(() => {
                //发送请求
                axios.get("/user/delete.do?id=" + row.id).then(response => {
                    this.$message({
                        message: response.data.message,
                        type: response.data.flag === true ? 'success' : 'error'
                    });
                    //刷新列表
                    this.findPage();
                });
            }).catch(()=>{

            });
        }
    }
});