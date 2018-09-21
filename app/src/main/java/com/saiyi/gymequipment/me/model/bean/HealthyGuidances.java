package com.saiyi.gymequipment.me.model.bean;

import java.util.List;

public class HealthyGuidances {
    private Number endRow;
    private Number firstPage; //第一页页码
    private boolean hasNextPage;//是否有下一页
    private boolean hasPreviousPage;//是否有上一页
    private boolean isFirstPage; //是否第一页;
    private boolean isLastPage; //是否最后一页;
    private Number lastPage; //	最后一页页码
    private List<Article> list;
    private Number navigatePages;
    private Number[] navigatepageNums;
    private Number nextPage;  //下一页页码
    private Number pageNum; //当前页码;
    private Number pageSize; //当前请求数据量;
    private Number pages; //总页数;
    private Number prePage; //上一页页码;
    private Number size; //总数据量;
    private Number startRow;
    private Number total;//总页数

    public List<Article> getList() {
        return list;
    }

    public Number getEndRow() {
        return endRow;
    }

    public Number getFirstPage() {
        return firstPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public Number getLastPage() {
        return lastPage;
    }

    public Number getNavigatePages() {
        return navigatePages;
    }

    public Number[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public Number getNextPage() {
        return nextPage;
    }

    public Number getPageNum() {
        return pageNum;
    }

    public Number getPageSize() {
        return pageSize;
    }

    public Number getPages() {
        return pages;
    }

    public Number getPrePage() {
        return prePage;
    }

    public Number getSize() {
        return size;
    }

    public Number getStartRow() {
        return startRow;
    }

    public Number getTotal() {
        return total;
    }
}
