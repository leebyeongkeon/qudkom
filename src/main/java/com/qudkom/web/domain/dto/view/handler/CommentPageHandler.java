package com.qudkom.web.domain.dto.view.handler;

import com.qudkom.web.domain.vo.board.post.comment.Comment;
import lombok.Data;

import java.util.*;

@Data
public class CommentPageHandler extends PageHandler {
    public static final int DEFAULT_SINGLE_PAGE_SIZE =10;//0;
    public static final int DEFAULT_PAGE_LIST_SIZE=5;

    public CommentPageHandler(int totalCount, int currentPage){
//        this(totalCount, currentPage, DEFAULT_SINGLE_PAGE_SIZE, DEFAULT_PAGE_LIST_SIZE);
        this(totalCount, currentPage, DEFAULT_SINGLE_PAGE_SIZE, DEFAULT_PAGE_LIST_SIZE);
    }
    public CommentPageHandler(int totalCount, int currentPage, int pageSize, int listSize){
        super(totalCount, currentPage, pageSize, listSize);

//        this.totalCount = totalCount;
//        this.pageSize=pageSize;
//        this.listSize=listSize;
//        setLastPage();
//        setCurrentPage(currentPage);
//        setHasPages();
//        setPageList();
    }
//    public void setPageList(){
//        if(this.totalCount==0){
////            this.pageList=new ArrayList<>(0);
//            return;
//        }
//        super.setPageList();
//        System.out.println("댓글 페이지 리스트");
//    }

    public static int calcLastPage(int totalCount, int pageSize){
        int lastPage = (totalCount/pageSize)
                +(totalCount%pageSize==0?0:1);
        if(lastPage==0) lastPage=1;
        return lastPage;
    }
    public static int calcLastPage(int totalCount){
        return calcLastPage(totalCount, DEFAULT_SINGLE_PAGE_SIZE);
    }

    @Deprecated
    private List<Comment> totalCommentList;
    @Deprecated
    public CommentPageHandler(List<Comment> totalCommentList) {
        this(totalCommentList,
                CommentPageHandler.DEFAULT_SINGLE_PAGE_SIZE,
                CommentPageHandler.DEFAULT_PAGE_LIST_SIZE);
//        this.pageSize=CommentPageHandler.DEFAULT_COMMENT_PAGE_SIZE;
//        this.listSize=CommentPageHandler.DEFAULT_PAGE_LIST_SIZE;
//        this.commentList=totalCommentList;
//        this.totalCommentCount=totalCommentList.size();
//        this.lastCommentPage = totalCommentCount/this.pageSize+1;
//        this.commentPageList=new ArrayList<>(this.pageSize);
//        setHasPages();
    }
    @Deprecated
    public CommentPageHandler(List<Comment> totalCommentList,int pageSize, int listSize){
        this.pageSize=pageSize;
        this.listSize=listSize;
        this.totalCommentList=totalCommentList;
        this.totalCount =totalCommentList.size();
        setLastPage();
        this.pageList =new ArrayList<>(this.listSize);
    }

//    public void setLastPage(){
//        this.lastPage =  (this.totalCount /this.pageSize)
//                +(this.totalCount %this.pageSize==0?0:1);
//    }

    @Deprecated
    public void setCommentPageByCommentNo(int commentNo){
        this.currentPage =getCommentBelongedPage(commentNo);
        setHasPages();
        setPageList();
    }

    @Deprecated
    public void setCommentPageByPaging(int targetCommentPage){
        if(targetCommentPage>this.lastPage || targetCommentPage==0)
            targetCommentPage=this.lastPage;

        //다음 페이지를 클릭했을때 해당 페이지의 댓글이 모두 삭제됐을 경우 마지막 페이지를 타겟팅함
        this.currentPage =targetCommentPage;
        setHasPages();
//        setCommentPageList();
        setPageList();
    }

    @Deprecated
    private int getCommentBelongedPage(int commentNo){
        int i=0;
        for(i=0; i<this.totalCount; i++){
            if(this.totalCommentList.get(i).getCommentNo()==commentNo)
                break;
        }
        return i/this.pageSize+1;
    }
    @Deprecated
    public List<Comment> extractCommentList(){
        List<Comment> resultList=new ArrayList<>(this.pageSize);
        if(this.totalCount ==0) return resultList;
        int startIdx=this.pageSize*(this.currentPage -1);
        int lastIdx=startIdx+this.pageSize;
        for(int i = startIdx; i<this.totalCount && i<lastIdx; i++){
            resultList.add(this.totalCommentList.get(i));
        }
        return resultList;
    }

//    public void setCommentPageList() {
//        if(this.totalCount ==0) return;
//        int start = (this.currentPage %this.listSize==0)
//                ?(this.currentPage -this.listSize+1)
//                :(this.currentPage -this.currentPage %this.listSize+1);
//        int end=start+this.listSize;
//        for(int i = start; i<end && i <= this.lastPage; i++){
//            this.pageList.add(i);
//        }
//    }

//    private void setHasPages(){
//        if((this.currentPage -1)/this.listSize==0){
////            this.hasFirst=false;
//            this.hasPrev=false;
//        }else{
//            this.hasPrev=true;
//        }
//        if((this.currentPage -1)/this.listSize==(this.lastPage -1)/this.listSize){
////            this.hasLast=false;
//            this.hasNext=false;
//        }else{
//            this.hasNext=true;
//        }
//    }

    @Override
    public String toString() {
        return "CommentPageHandler{" +
                "pageSize=" + pageSize +
                ", listSize=" + listSize +
                ", currentPage=" + currentPage +
                ", lastPage=" + lastPage +
                ", totalCount=" + totalCount +
                ", pageList=" + pageList +
                ", hasPrev=" + hasPrev +
                ", hasNext=" + hasNext +
                '}';
    }
}
