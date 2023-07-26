import axios from "axios";

export class Tool {
    /**
     * 空校验 null或""都返回true
     */
    public static isEmpty (obj: any) {
        if ((typeof obj === 'string')) {
            return !obj || obj.replace(/\s+/g, "") === ""
        } else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
        }
    }

    /**
     * 非空校验
     */
    public static isNotEmpty (obj: any) {
        return !this.isEmpty(obj);
    }

    /**
     * 对象复制
     * @param obj
     */
    public static copy (obj: object) {
        if (Tool.isNotEmpty(obj)) {
            return JSON.parse(JSON.stringify(obj));
        }
    }

    /**
     * 使用递归将数组转为树形结构
     * 父ID属性为parent
     */
    public static array2Tree (categorys: any, parentId: number) {
        const array = Tool.copy(categorys);
        if (Tool.isEmpty(array)) {
            return [];
        }
        const result = [];
        for (let i = 0; i < array.length; i++) {
            const c = array[i];
            // console.log(Number(c.parent), Number(parentId));
            if (Number(c.parent) === Number(parentId)) {
                // console.log(c);
                result.push(c);

                // 递归查看当前节点对应的子节点
                const children = Tool.array2Tree(array, c.id);
                if (Tool.isNotEmpty(children)) {
                    c.children = children;
                }
            }
        }
        return result;
    }
    /**
     * 将父分类的id，变为父分类的名字
     *
     */
    public static parentIdToParentName(array:any){
        for(let i=0;i<array.length;i++){
            const c = array[i];
            console.log(c.name);
            c.parent = "无";
            if(Tool.isNotEmpty(c.children)){
                for(let j=0;j<c.children.length;j++){
                    c.children[j].parent = c.name;
                }
            }

        }
        return array;
    }

    /**
     * 将父分类的名字，变为父分类的id
     */
    public static parentNameToParentId(category:any , array:any){
        if(category.parent === "无"){
            category.parent = 0;
            return category;
        }
        for(let i=0;i<array.length;i++){
            if(category.parent === array[i].name) category.parent = Number(array[i].id);

        }
        return category;
    }

    /**
     * 分类查询显示
     *
     */
    public static keySearch (array: any,categorys: any){
        const v = [];
        for(let i=0;i<categorys.length;i++){
            if(Tool.isNotEmpty(array)){
                for(let j=0;j<array.length;j++){
                    if(Number(array[j].id) === Number(categorys[i].id)) v[i] = 1;
                    if(Tool.isNotEmpty(array[j].children)){
                        const c = array[j].children;
                        for(let k=0;k<c.length;k++){
                            if(Number(c[k].id)===Number(categorys[i].id)) v[i] = 1;
                        }
                    }
                }
            }
        }
        for(let i=0;i<categorys.length;i++){
            if(v[i] != 1){
                axios.get("/category/list",{
                    params:{
                        id:categorys[i].parent
                    }
                }).then((response)=>{
                    const data = response.data;

                    if(data.success){
                        categorys[i].parent = data.content.list[0].name;
                    }
                })
                array.push(categorys[i]);
            }
        }
        return array;
    }

    /**
     * 随机生成[len]长度的[radix]进制数
     * @param len
     * @param radix 默认62
     * @returns {string}
     */
    public static uuid (len: number, radix = 62) {
        const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        const uuid = [];
        radix = radix || chars.length;

        for (let i = 0; i < len; i++) {
            uuid[i] = chars[0 | Math.random() * radix];
        }

        return uuid.join('');
    }
}