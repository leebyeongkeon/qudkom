package com.qudkom.web.service.board.post.comment;

import com.qudkom.web.dao.board.post.PostDao;
import com.qudkom.web.dao.board.post.comment.CommentDao;
import com.qudkom.web.dao.board.post.comment.CommentLikeDao;
import com.qudkom.web.domain.dto.extended.board.post.comment.CommentDto;
import com.qudkom.web.domain.dto.param.FieldBundle;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.param.SqlStr;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.dto.param.bundle.CommentParamBundle;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.board.post.comment.CommentLike;
import com.qudkom.web.domain.dto.view.handler.CommentPageHandler;
import com.qudkom.web.domain.dto.view.CommentViewDto;
import com.qudkom.web.exception.AlreadyVotedException;
import com.qudkom.web.service.user.alarm.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    CommentLikeDao commentLikeDao;
    @Autowired
    PostDao postDao;
//    @Autowired
//    AlarmDao alarmDao;
    @Autowired
    AlarmService alarmService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertComment(Comment comment) throws SQLException{
        int rowCnt=commentDao.insert(comment);
        if(rowCnt==0)
            throw new SQLException();
        SqlStr sqlStr=SqlStr.builder()
                .field(Post.COMMENT_COUNT_FIELD_NAME)
                .sign("+")
                .build();
        FieldBundle fieldBundle=FieldBundle.builder()
                .boardNo(comment.getBoardNo())
                .postNo(comment.getPostNo())
                .build();
        BoardParamBundle boardParamBundle = BoardParamBundle.builder()
                .fieldBundle(fieldBundle)
                .sqlStr(sqlStr)
                .build();
        rowCnt=postDao.updateCountFieldInPost(boardParamBundle);
        if(rowCnt==0)
            throw new SQLException();

        return rowCnt;
    }
    // ????????? ??????, ????????? ????????? ?????? ???????????? ????????? ??????
    // ???????????? ?????? ????????? ??????, ????????? ??????, ?????? ????????? ????????? ????????? ????????????,
    // ?????? ????????? ???????????? ????????? ???????????? ?????? ????????? ????????? ??????


    @Deprecated
    @Override
    public CommentViewDto writeComment(Comment comment) throws SQLException {
        this.insertComment(comment);
        CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                .comment(comment)
                .commentParam(comment.getCommentNo()).usePage(false).build();
        CommentViewDto commentViewDto=this.getCommentList(commentParamBundle);
        commentViewDto.setNewCommentNo(comment.getCommentNo());
        return commentViewDto;
    }

    @Deprecated
    @Override
    public CommentViewDto getCommentList(CommentParamBundle commentParamBundle){
        List<Comment> sortedCommentList=this.commentDao.selectList(commentParamBundle);

        CommentPageHandler commentPageHandler=new CommentPageHandler(sortedCommentList);
        if(commentParamBundle.getUsePage()) {
            commentPageHandler.setCommentPageByPaging(commentParamBundle.getCommentParam());
        }else {
            commentPageHandler.setCommentPageByCommentNo(commentParamBundle.getCommentParam());
        }
        CommentViewDto commentViewDto=new CommentViewDto();
        commentViewDto.setCommentList(commentPageHandler.extractCommentList());
        commentViewDto.setCommentPageHandler(commentPageHandler);
        commentViewDto.setCompletelyErased(commentParamBundle.getCompletelyErased());
        return commentViewDto;
    }
    @Deprecated
    @Override
    public CommentViewDto editComment(Comment comment) throws SQLException {
        comment.setStatus(1);
        int rowCnt = commentDao.update(comment);
        if(rowCnt==0)
            throw new SQLException();
        CommentParamBundle commentParamBundle = CommentParamBundle.builder().comment(comment)
                .commentParam(comment.getCommentNo()).usePage(false).build();
        return this.getCommentList(commentParamBundle);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteComment(Comment comment) throws SQLException{
        comment=commentDao.select(comment);
        if(comment==null)
            throw new SQLException();//??????????????? ????????? ???????????? ??????
        int rowCnt=0;

        if(commentDao.selectExistsChild(comment)){

//            int voteCount=comment.getLikeCount()+comment.getDislikeCount();
//            rowCnt=commentLikeDao.deleteList(comment);
//            if(voteCount!=rowCnt) throw new SQLException();

            comment.setStatus(4);
            comment.setContent("");
            rowCnt=commentDao.updateStatus(comment);
//            ?????? ??? ?????? ???????????? ?????? ??????
            if(rowCnt==0)
                throw new SQLException();
            //?????? ????????? ?????? ????????? 4??? ????????? ????????? ???????????? -1 ??????, ????????? ?????? ??????
            return -1;
        }
        else {
            rowCnt = commentDao.delete(comment);
            if(rowCnt==0)
                throw new SQLException();
            SqlStr sqlStr=SqlStr.builder()
                    .field(Post.COMMENT_COUNT_FIELD_NAME)
                    .sign("-")
                    .build();
            FieldBundle fieldBundle=FieldBundle.builder()
                    .boardNo(comment.getBoardNo())
                    .postNo(comment.getPostNo())
                    .build();
            BoardParamBundle boardParamBundle = BoardParamBundle.builder()
//                    .boardNo(comment.getBoardNo())
//                    .postNo(comment.getPostNo())
                    .fieldBundle(fieldBundle)
                    .sqlStr(sqlStr)
//                    .field(Post.COMMENT_COUNT_FIELD_NAME)
//                    .sign("-")
                    .build();
            rowCnt = postDao.updateCountFieldInPost(boardParamBundle);
            if(rowCnt==0)
                throw new SQLException();
            return rowCnt;
        }
    }
    @Override
    public CommentViewDto eraseComment(Comment comment) throws SQLException{
//        comment ????????? delete, post ????????? ????????????
        int result=this.deleteComment(comment);
        CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                .comment(comment)
                .build();
        if(result==-1) {//?????? ????????? ?????? ????????? ????????? ?????? ?????? ??? ????????? ????????? ???????????????
            commentParamBundle.setUsePage(false);
            commentParamBundle.setCompletelyErased(false);
            commentParamBundle.setCommentParam(comment.getCommentNo());
        }else{//?????? ????????? ?????? ????????? ????????? ?????? ????????? ?????? ????????????, ????????? ????????????
            commentParamBundle.setUsePage(true);
            commentParamBundle.setCompletelyErased(true);
            commentParamBundle.setCommentParam(0);
        }
        System.out.println(comment);
        System.out.println(commentParamBundle);
        return this.getCommentList(commentParamBundle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCommentLike(CommentLike commentLike, String sign) throws SQLException {
        int rowCnt=0;
        if(sign.equals("+"))//????????? ?????? ????????????, ?????? ?????????/???????????? ??????
            rowCnt = commentLikeDao.insert(commentLike);
        else
            rowCnt = commentLikeDao.delete(commentLike);
        if(rowCnt==0)//?????? insert ?????? delete??? ????????? ??????
            throw new SQLException();
        String field=Comment.LIKE_COUNT_FIELD_NAME;
        if(!commentLike.getIsLike())
            field=Comment.DISLIKE_COUNT_FIELD_NAME;
        SqlStr sqlStr=SqlStr.builder()
                .field(field)
                .sign(sign)
                .build();
        FieldBundle fieldBundle=FieldBundle.builder()
                .boardNo(commentLike.getBoardNo())
                .postNo(commentLike.getPostNo())
                .commentNo(commentLike.getCommentNo())
                .build();
        BoardParamBundle boardParamBundle=BoardParamBundle.builder()
//        QueryParamBundle queryParamBundle = QueryParamBundle.builder()
//                .boardNo(commentLike.getBoardNo())
//                .postNo(commentLike.getPostNo())
//                .commentNo(commentLike.getCommentNo())
                .fieldBundle(fieldBundle)
                .sqlStr(sqlStr)
//                .sign(sign).field(field)
                .build();
        rowCnt = commentDao.updateCountField(boardParamBundle);
        if(rowCnt==0)
            throw new SQLException();
        return rowCnt;
    }
    @Override
    public Comment voteComment(CommentLike commentLike) throws SQLException, AlreadyVotedException {
        if(commentLikeDao.selectExists(commentLike))
            throw new AlreadyVotedException();//?????? ????????? ??????
        this.updateCommentLike(commentLike,"+");
        Comment comment=commentDao.selectLikeCount(commentLike);
        return comment;
    }
    @Override
    public Comment cancelVoteComment(CommentLike commentLike) throws SQLException{
//        if(!commentLikeDao.selectExists(commentLike))
        commentLike=commentLikeDao.select(commentLike);
        if(commentLike==null)
            throw new SQLException();
        //????????? ???????????? ????????? 1??? ??????????????? rowCnt??? ????????????
        this.updateCommentLike(commentLike,"-");
        Comment comment=commentDao.selectLikeCount(commentLike);
        return comment;
    }

    /////////////////////////////////////////////////////////////

    //?????? ??????, ?????????, ????????? ??????, ??????, ?????? ???
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentViewDto writeCommentCTE(CommentParamBundle commentParamBundle) throws SQLException {
        Comment comment= commentParamBundle.getComment();
        this.insertComment(comment);
        int alarmTargetUser=alarmService.addAlarm(comment);
        CommentViewDto commentViewDto=this.getCommentCTE(commentParamBundle);
//        if(alarmTargetUser!=0){
            commentViewDto.setAlarmTargetUser(alarmTargetUser);
//        }
        commentViewDto.setNewCommentNo(comment.getCommentNo());

        return commentViewDto;
    }

    @Override
    public CommentViewDto eraseCommentCTE(CommentParamBundle commentParamBundle) throws SQLException {
//        Comment comment=commentParamDto.getComment();
        Comment comment= commentParamBundle.getComment();
        System.out.println("erase comment = " + comment);
        int result=0;
        int count=0;
        // ?????? ?????? ????????? ?????? ????????? ??????????????? ????????????
        // ?????? ????????? ????????? 4?????? ????????? ??? ?????? ????????? ?????????
        while(result!=-1) {
            result = this.deleteComment(comment);
            count++;
            if(result!=1 || comment.getParentComment()==null) break;
            Comment parentComment=commentDao.selectParentComment(comment);
            if(parentComment==null || parentComment.getStatus()!=4) break;
            comment=parentComment;
//        CommentParamDto commentParamDto=CommentParamDto.builder().boardNo(commentParamDto.getBoardNo())
//                .postNo(commentParamDto.getPostNo()).build();
        }
        if(result==-1 && count==1) {//?????? ????????? ?????? ????????? ????????? ?????? ?????? ??? ????????? ????????? ???????????????
            commentParamBundle.setUsePage(false);
            commentParamBundle.setCompletelyErased(false);
        }else{//?????? ????????? ?????? ????????? ????????? ?????? ????????? ?????? ????????????, ????????? ????????????
            Paging paging= commentParamBundle.getPaging();
            int pageSize=paging.getPageSize();
            commentParamBundle.setUsePage(true);
            commentParamBundle.setCompletelyErased(true);
            int commentCount=postDao.selectCommentCount(comment);
            int page=CommentPageHandler.calcLastPage(commentCount, pageSize);
//            commentParamDto.setPage(page);
            paging.setPage(page);
//            if(page==0) page=1;
            paging.setOffset((page-1)*pageSize);
        }
        System.out.println(commentParamBundle);
        return this.getCommentCTE(commentParamBundle);
    }

    @Override
    public CommentViewDto editCommentCTE(CommentParamBundle commentParamBundle) throws SQLException {
        Comment comment= commentParamBundle.getComment();
        comment.setStatus(1);
        int rowCnt = commentDao.update(comment);
        if(rowCnt==0)
            throw new SQLException();
        return this.getCommentCTE(commentParamBundle);
    }

    @Override
    public CommentViewDto getCommentCTE(CommentParamBundle commentParamBundle){
        Paging paging= commentParamBundle.getPaging();
        int pageSize=paging.getPageSize();
        int listSize=paging.getListSize();
        Integer totalCount=paging.getTotalCount();
        if(totalCount==null ||totalCount==0){
//            System.out.println("paging2 = " + paging);
            totalCount=postDao.selectCommentCount(commentParamBundle.getComment());
            paging.setTotalCount(totalCount);
        }
        if(commentParamBundle.getUsePage() && paging.getPage()==0) {
            //0??? ?????? ????????? ???????????? ??????.
            //?????????????????? ???????????? 0?????? ?????????.
            int commentPage = CommentPageHandler.calcLastPage(totalCount, pageSize);
            paging.setPage(commentPage);
            paging.setOffset((commentPage-1)*pageSize);
        }
        List<CommentDto> commentList = commentParamBundle.getUsePage()
                ?getCommentListCTE(commentParamBundle)
                :getCommentListByNo(commentParamBundle);
        System.out.println(commentParamBundle);
        CommentPageHandler cph=new CommentPageHandler(paging.getTotalCount(),paging.getPage(),pageSize,listSize);
        System.out.println(cph);
        CommentViewDto commentViewDto=new CommentViewDto();
        if(commentParamBundle.getCompletelyErased()!=null)
            commentViewDto.setCompletelyErased(commentParamBundle.getCompletelyErased());
        commentViewDto.setCommentDtoList(commentList);
        commentViewDto.setCommentPageHandler(cph);
        return commentViewDto;
    }

    @Override
    public boolean checkExists(Comment comment) {
        return commentDao.selectExists(comment);
    }

    //    @Override
    private List<CommentDto> getCommentListCTE(CommentParamBundle CommentParamBundle){
        return commentDao.selectListCTE(CommentParamBundle);
    }
    @Override
    public List<CommentDto> getCommentListByNo(CommentParamBundle commentParamBundle){

        int page=calcCommentPage(commentParamBundle);
        Paging paging= commentParamBundle.getPaging();
        paging.setPage(page);
        paging.setOffset((page-1)*paging.getPageSize());
//        commentParamDto.setPaging(paging);

        return getCommentListCTE(commentParamBundle);
    }
    @Override
    public int calcCommentPage(CommentParamBundle commentParamBundle){
        Comment comment= commentParamBundle.getComment();
        Paging paging= commentParamBundle.getPaging();
        List<Integer> noList=commentDao.selectNoList(comment);
        System.out.println("commentParamDto = " + commentParamBundle);
        noList.stream().forEach(n->System.out.print(n+" "));
        int pageSize=paging.getPageSize();

        int page=1;
        int size=noList.size();
        int commentNo= comment.getCommentNo();

        for(int i=0, p=0;i<size;i++, p++){
            System.out.println("p = " + p);
            System.out.println("i = " + i);
            System.out.println();
            if(p==pageSize){
                page++;
                p=0;
            }
            if(commentNo==noList.get(i)){
                break;
            }
        }
        System.out.println(page);
        return page;
    }
}
