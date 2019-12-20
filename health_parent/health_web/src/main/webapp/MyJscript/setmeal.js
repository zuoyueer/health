/*!
 * 对应 page/setmeal.html 的js文件
 * ---------------------------------
 * 主要是对套餐的增删改查
 */
new Vue({
    el: '#app',
    data: {
        autoUpload: true,//自动上传
        imageUrl: null,//模型数据，用于上传图片完成后图片预览
        activeName: 'first',//添加/编辑窗口Tab标签名称
        pagination: {//分页相关属性
            currentPage: 1,
            pageSize: 5,
            total: 100,
            queryString: null,
        },
        dataList: [],//列表数据
        formData: {},//表单数据
        tableData: [],//添加表单窗口中检查组列表数据
        checkgroupIds: [],//添加表单窗口中检查组复选框对应id
        dialogFormVisible: false,//控制添加窗口显示/隐藏
        dialogFormVisible4Edit: false//控制编辑窗口显示/隐藏
    },
    created() {
        this.findPage();
    },
    methods: {
        //文件上传成功后的钩子，response为服务端返回的值，file为当前上传的文件封装成的js对象
        handleAvatarSuccess(response, file) {
            this.imageUrl = "http://q26n0zocl.bkt.clouddn.com/" + response.data;
            this.$message({
                message: response.message,
                type: response.flag === true ? 'success' : 'error'
            });
            //设置模型数据(图片名称),后续提交ajax请求时,会提交到后台,最终存储到数据库
            console.log(response.data);
            console.log(this.imageUrl);
            console.log(response.flag);
            this.formData.img = response.data;
        },
        //上传图片之前执行的钩子
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG) {
                this.$message.error('上传套餐图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传套餐图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        },
        //添加
        handleAdd() {
            //发送请求,提交数据: 选中的检查项组的id集合, 套餐的数据
            axios.post("/setmeal/add.do?checkgroupIds=" + this.checkgroupIds, this.formData).then(response => {
                //关闭窗口
                this.dialogFormVisible = false;
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
        //分页查询
        findPage() {
            var param = {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                queryString: this.pagination.queryString
            };
            axios.post("/setmeal/findPage.do", param).then(response => {
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
            this.activeName = 'first';
            this.checkgroupIds = [];
            this.imageUrl = null;
        },
        // 弹出添加窗口
        handleCreate() {
            this.dialogFormVisible = true;
            this.resetForm();
            axios.get("/checkgroup/findAll.do").then(response => {
                if (response.data.flag) {
                    this.tableData = response.data.data;
                } else {
                    this.$message.error(response.data.message);
                }
            });
        },
        //切换页码
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.findPage();
        },
        //删除
        handleDelete(row) {
            this.$confirm("确定要删除选择的检查组吗?", "提示", {type: "warning"}).then(() => {
                axios.get("/setmeal/delete.do?id=" + row.id).then(response => {
                    this.$message({
                        message: response.data.message,
                        type: response.data.flag === true ? 'success' : 'error'
                    });
                    //刷新列表
                    this.findPage();
                });
            }).catch(()=>{

            });
        },
        //弹出编辑框
        handleUpdate(row) {
            this.dialogFormVisible4Edit = true;
            this.activeName = 'first';
            //查询套餐数据
            axios.get("/setmeal/findById.do?id=" + row.id).then(response => {
                if (response.data.flag) {
                    //给套餐数据赋值
                    this.formData = response.data.data;
                    //查询全部检查组
                    axios.get("/checkgroup/findAll.do").then(response => {
                        if (response.data.flag) {
                            //给检查组赋值
                            this.tableData = response.data.data;
                            //查询套餐绑定的检查组的id的集合
                            axios.get("/setmeal/findCheckGroupIdsBySetmealId.do?id=" + row.id).then(response => {
                                //给检查组id集合赋值, 注意是response.data而不是response.data.data
                                this.checkgroupIds = response.data;
                            });
                        } else {
                            this.$message.error(response.data.message);
                        }
                    });
                } else {
                    this.$message.error(response.data.message);
                }
            });
        },
        //提交修改
        handleEdit() {
            axios.post("/setmeal/edit.do?checkgroupIds=" + this.checkgroupIds, this.formData).then(response => {
                //隐藏编辑框
                this.dialogFormVisible4Edit = false;
                //提示信息
                this.$message({
                    message: response.data.message,
                    type: response.data.flag === true ? 'success' : 'error'
                });
            }).finally(() => {
                this.findPage();
            });
        }
    }
});