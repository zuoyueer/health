let vm = new Vue({
    el: "#app",
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
    methods: {
        //添加
        handleAdd() {
            this.$refs['dataAddForm'].validate(valid => {
                if (valid) {
                    axios.post("/checkitem/add.do", this.formData).then(response => {
                        this.dialogFormVisible = false;
                        this.$message({
                            message: response.data.message,
                            type: response.data.flag === true ? 'success' : 'error'
                        });
                    }).finally(() => {
                        this.findPage();
                    });

                } else {
                    this.$message.error("表单数据校验失败");
                }
            });
        },
        findPage() {
            var page = {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                queryString: this.pagination.queryString
            };
            axios.post("/checkitem/findPage.do", page).then(response => {
                this.dataList = response.data.rows;
                this.pagination.total = response.data.total;
            });
        },
        findPageByCondition() {
            this.pagination.currentPage = 1;
            this.findPage();
        },
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.findPage();
        },
        //弹出添加窗口
        handleCreate() {
            this.resetForm();
            this.dialogFormVisible = true;
        },
        //弹出编辑窗口
        handleUpdate(row) {
            this.dialogFormVisible4Edit = true;
            axios.get("/checkitem/findById.do?id=" + row.id).then(response => {
                this.$message({
                    message: response.data.message,
                    type: response.data.flag === true ? 'success' : 'error'
                });
                this.formData = response.data.data;
            });
             //this.formData = row;
        },
        //重置表单
        resetForm() {
            this.formData = {};
        },
        handleDelete(row) {
            this.$confirm("确认删除当前选中记录吗?", "删除提示", {type: "warning"}).then(() => {
                axios.get("/checkitem/delete.do?id=" + row.id).then(response => {
                    this.$message({
                        message: response.data.message,
                        type: response.data.flag === true ? 'success' : 'error'
                    });
                    this.findPage();
                });
            });
        },
        handleEdit() {
            this.$refs['dataEditForm'].validate((valid) => {
                if (valid) {
                    this.dialogFormVisible4Edit = false;
                    axios.post("/checkitem/edit.do", this.formData).then((response) => {
                        this.findPage();
                        this.$message({
                            message: response.data.message,
                            type: response.data.flag === true ? 'success' : 'error'
                        });
                    });
                } else {
                    this.$message.error("校验失败,请输入正确格式的数据");
                }
            });
        }
    },
    created: function () {
        this.findPage();
    }
});