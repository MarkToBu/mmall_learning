 idea配置git

#### git init 

#### git add .

#### git status

#### git commit  -am   'first commit init  project ' 

#### git   remote add origin git@git........

#### git branch

#### git push -u orgin master

#### git pull

#### git push -u origin master        # 推送

#### git push -u -f origin mater       # 强制推送

git branch       # 查看本地分支

git branch -r    # 查看元成功分支

git checkout -b   v1.0 origin/mater

git  branch

git push origin HEAD -u



----------------

1、先拉下来，会自动合并的（不用操心）

git pull origin master

2、再上传

git push -u origin master

### idea 快捷键

ctrl + N	查找类别

ctrl + O	重写类的固有 方法

ctrl + t  /  ctrl+alt + {鼠标左键}  查找接口方法实现

ctrl +shirt + i  小窗口查看调用方法的信息



---

set的contains方法的复杂度是O1，list的复杂度是On；

## 常用代码

```
PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8")
```

 使用pagehelper时，xml中的sql语句不能写分号；

#### mybatis

```xml
 <select id="selectByNameAndProductId" resultMap="BaseResultMap" parameterType="map">
    select 
    <include refid="Base_Column_List"/>
    from mmall_product
    <where>
      <if test="productName != null">
        and name like #{productName}
      </if>
      <if  test="productId != null">
        and id = #{productId}
      </if>
    </where>

  </select>
```
pagehelper分页排序

```java
 <select id="selectByNameAndCategoryIds" resultMap="BaseResultMap" parameterType="map">
    select
      <include refid="Base_Column_List"></include>
    from mmall_product
    where status = 1
    <if test="productName != null">
      and name like #{productName}
    </if>
    <if test="categoryIdList != null">
      and category_id in
      <foreach item="item" index="index" open="(" separator="," collection="categoryIdList" close=")">
        #{item}
      </foreach>
    </if>
  </select>
   	
   	
   	@Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc()) ;

        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                //没有该分类，无关键字，返回一个空的结果集。
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageinfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageinfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(categoryId).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" " + orderByArray[1]);
             //Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc"); }
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo>  productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProducListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return ServerResponse.createBySuccess(pageInfo);

    }
```

### 调用支付宝
####  额外的jar包配置
```
 <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
          <encoding>UTF-8</encoding>
          <compilerArguments>
            <extdirs>${project.basedir}/src/main/webapp/WEB-INF/lib</extdirs>
          </compilerArguments>
        </configuration>
      </plugin>
      
  ```