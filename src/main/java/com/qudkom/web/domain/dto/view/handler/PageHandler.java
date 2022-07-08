package com.qudkom.web.domain.dto.view.handler;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageHandler {
    public static final int DEFAULT_SINGLE_PAGE_SIZE=20;
    public static final int DEFAULT_PAGE_LIST_SIZE=10;
    public static final int INDEX_BOARD_CARD_SIZE=5;

    protected int pageSize;
    protected int listSize;

    protected int currentPage;
    protected int start;
    protected int end;
    protected int lastPage;
    protected int totalCount;

    protected List<Integer> pageList;
//    private boolean hasFirst;
    protected boolean hasPrev;
    protected boolean hasNext;
//    private boolean hasLast;
    public PageHandler(){}
    public PageHandler(int totalCount, int currentPage){
        this(totalCount,currentPage,DEFAULT_SINGLE_PAGE_SIZE, DEFAULT_PAGE_LIST_SIZE);
    }
    public PageHandler(int totalCount, int currentPage, int pageSize, int listSize){
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.listSize = listSize;
        setLastPage();
        setCurrentPage(currentPage);
        setHasPages();
        setPageList();
    }
    protected void setLastPage(){
//        this.lastPage=this.totalCount /this.pageSize +1;
        this.lastPage =  (this.totalCount/this.pageSize)
                +(this.totalCount%this.pageSize==0?0:1);
    }

    protected void setCurrentPage(int currentPage) {
        if(currentPage<1)
            currentPage=1;
        if(currentPage>this.lastPage)
            currentPage=this.lastPage;
        this.currentPage = currentPage;
    }

    protected void setHasPages(){
        if((this.currentPage-1)/this.listSize ==0){
//            this.hasFirst=false;
            this.hasPrev=false;
        }else{
            this.hasPrev=true;
        }
        if((this.currentPage-1)/this.listSize ==(this.lastPage-1)/this.listSize){
//            this.hasLast=false;
            this.hasNext=false;
        }else{
            this.hasNext=true;
        }
    }
    protected void setPageList() {
//        if(this.totalCount==0){ this.pageList=new ArrayList<>(0); return;};
        this.pageList=new ArrayList<>(this.listSize);
        this.start= this.currentPage%this.listSize ==0 ?
                        (this.currentPage-1)/this.listSize *this.listSize +1
                        :this.currentPage-this.currentPage%this.listSize +1;
        this.end=start+this.listSize -1;
        if(end>this.lastPage) end=this.lastPage;
        for(int i=start; i <=end; i++){
            this.pageList.add(i);
        }
        System.out.println("페이지 리스트");
    }

    @Override
    public String toString() {
        return "PageHandler{" +
                "pageSize=" + pageSize +
                ", listSize=" + listSize +
                ", currentPage=" + currentPage +
                ", lastPage=" + lastPage +
                ", totalCount=" + totalCount +
                ", pageList=" + pageList +
                ", hasPrev=" + hasPrev +
                ", hasNext=" + hasNext +
                ", start="+start+
                ", end="+end+
                '}';
    }
}
