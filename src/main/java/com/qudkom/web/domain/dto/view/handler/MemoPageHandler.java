package com.qudkom.web.domain.dto.view.handler;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemoPageHandler extends PageHandler {
    public final static int DEFAULT_SINGLE_PAGE_SIZE =10;
    public final static int DEFAULT_PAGE_LIST_SIZE =5;

//    private Integer totalCount;
//    private Integer pageSize;
//    private Integer listSize;
//    private Boolean hasPrev;
//    private Boolean hasNext;
//    private Integer lastPage;
//    private Integer currentPage;
//    private List<Integer> memoPageList;

    public MemoPageHandler(int totalCount, int currentPage){
        this(totalCount, currentPage, DEFAULT_SINGLE_PAGE_SIZE, DEFAULT_PAGE_LIST_SIZE);
    }
    public MemoPageHandler(int totalCount, int currentPage, int pageSize, int listSize){
        super(totalCount,currentPage,pageSize,listSize);
//        this.totalCount=totalCount;
//        this.pageSize=pageSize;
//        this.listSize=listSize;
//        setLastPage();
//        setHasPages();
//        setPageList();
    }
//    public void setCurrentPage(int currentPage){
//        if(currentPage<1)
//            currentPage=1;
//        if(currentPage>this.lastPage)
//            currentPage=this.lastPage;
//        this.currentPage=currentPage;
//    }
//    private void setPageList() {
//        if(this.totalCount==0) return;
//        this.pageList=new ArrayList<>();
//        int start = (this.currentPage %this.listSize==0)
//                ?(this.currentPage -this.listSize+1)
//                :(this.currentPage -this.currentPage %this.listSize+1);
//        int end=start+this.listSize;
//        for(int i=start;i<end && i <= this.lastPage;i++){
//            this.pageList.add(i);
//        }
//    }

//    private void setHasPages() {
//        if((this.currentPage -1)/this.listSize==0){
////            this.hasFirst=false;
//            this.hasPrev=false;
//        }else{
//            this.hasPrev=true;
//        }
//        if((this.currentPage -1)/this.listSize==(this.lastPage-1)/this.listSize){
////            this.hasLast=false;
//            this.hasNext=false;
//        }else{
//            this.hasNext=true;
//        }
//    }

//    private void setLastPage() {
//        this.lastPage =  (this.totalCount/this.pageSize)
//                +(this.totalCount%this.pageSize==0?0:1);
//    }
}
