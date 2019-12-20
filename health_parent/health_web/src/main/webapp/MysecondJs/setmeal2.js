/*!
 * 对应 page/seteaml.html 的js文件
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
            console.log(response.data.flag);
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

        },
        //分页查询
        findPage() {

        },
        findPageByCondition() {
            this.pagination.currentPage = 1;
            this.findPage();
        },
        // 重置表单
        resetForm() {

        },
        // 弹出添加窗口
        handleCreate() {
            this.dialogFormVisible = true;
            this.resetForm();
            axios.get("/checkgroup/findAll.do").then(response=>{
                this.$message({
                    message: response.data.message,
                    type: response.data.flag ===true?'success':"error"
                });
                if (response.data.flag){
                    this.tableData = response.data.data;
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

        },
        //弹出编辑框
        handleUpdate(row) {

        },
        //提交修改
        handleEdit() {
        }
    }

});