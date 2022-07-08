package com.qudkom.web.service.board;

import com.qudkom.web.dao.board.BoardDao;
import com.qudkom.web.dao.board.BoardCategoryDao;
import com.qudkom.web.dao.board.post.PostCategoryDao;
import com.qudkom.web.dao.board.post.PostDao;
import com.qudkom.web.dao.board.post.PostLikeDao;
import com.qudkom.web.dao.board.post.comment.CommentDao;
import com.qudkom.web.dao.board.post.image.ImageDao;
import com.qudkom.web.domain.dto.extended.board.BoardDto;
import com.qudkom.web.domain.dto.extended.board.post.PostDto;
import com.qudkom.web.domain.dto.extended.board.post.image.ImageDto;
import com.qudkom.web.domain.dto.param.*;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.dto.param.bundle.CommentParamBundle;
import com.qudkom.web.domain.dto.view.handler.CommentPageHandler;
import com.qudkom.web.domain.dto.view.handler.PageHandler;
import com.qudkom.web.domain.vo.board.Board;
import com.qudkom.web.domain.vo.board.BoardCategory;
import com.qudkom.web.domain.dto.view.MenuDto;
import com.qudkom.web.domain.dto.view.BoardViewDto;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.PostCategory;
import com.qudkom.web.domain.vo.board.post.PostLike;
import com.qudkom.web.domain.dto.view.CommentViewDto;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.board.post.image.Image;
import com.qudkom.web.exception.AlreadyVotedException;
import com.qudkom.web.service.board.post.comment.CommentService;
import javassist.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    BoardCategoryDao boardCategoryDao;
    @Autowired
    BoardDao boardDao;
    @Autowired
    PostCategoryDao postCategoryDao;
    @Autowired
    PostDao postDao;
    @Autowired
    PostLikeDao postLikeDao;
    @Autowired
    CommentDao commentDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    CommentService commentService;

    @Autowired
    ServletContext context;

    @PostConstruct
    public void initMenu(){
        List<MenuDto> menuDtoList=this.getBoardMenu(1);
        context.setAttribute("menuList",menuDtoList);
    }

    @Override
    public List<MenuDto> getBoardMenu(int status){
        List<BoardCategory> boardCategoryList = boardCategoryDao.selectBoardCategoryList(status);
        //조회 가능한 카테고리들 가져온다
        int size=boardCategoryList.size();
        List<MenuDto> menuDtoList=new ArrayList<>(size);
        for(int i=0;i<size;i++){
//            menuDtoList.add(new MenuDto());
            MenuDto menu=new MenuDto();
            BoardCategory boardCategory=boardCategoryList.get(i);
            menu.setBoardCategory(boardCategory);
            List<Board> boardList=boardDao.selectAccessibleBoardList(boardCategory);
            menu.setBoardList(boardList);
            menuDtoList.add(menu);
//            menuDtoList.get(i).setBoardCategory(boardCategory);
//            menuDtoList.get(i).setBoardList(boardDao.selectAccessibleBoardList(boardCategory.getCategoryNo(),status));
        }
        return menuDtoList;
    }

    @Override
    public BoardDto getBoardDto(int boardNo) throws NotFoundException {
        Board board=boardDao.selectBoardByBoardNo(boardNo);
        if(board==null){
//            System.out.println("존재하지 않는 게시판입니다.");
            throw new NotFoundException("Not Found");
        }

        BoardCategory boardCategory = boardCategoryDao.select(board.getBoardCategoryNo());
        BoardDto boardDto=new BoardDto(board,boardCategory.getCategoryName());

        return boardDto;
    }

    private int getPostCount(BoardParamBundle boardParamBundle){
//        QueryParamBundle queryParamBundle = ViewParamBundle.toQueryParamDto(viewParamBundle);
//        boardParamBundle.set
        int count=0;
        if(checkSearchQueryParam(boardParamBundle)){//검색 결과 수
            count=postDao.selectSearchedCountInBoard(boardParamBundle);
        }else{
            count=postDao.selectCount(boardParamBundle.getFieldBundle().getBoardNo());
        }
        return count;

    }

    @Override
    public BoardViewDto getBoardView(BoardParamBundle boardParamBundle) throws NotFoundException {
        BoardViewDto boardViewDto = new BoardViewDto();
        BoardDto boardDto=this.getBoardDto(boardParamBundle.getFieldBundle().getBoardNo());
        boardViewDto.setBoardDto(boardDto);
        List<PostCategory> postCategoryList = postCategoryDao.selectList(boardDto.getBoardCategoryNo());
        boardViewDto.setPostCategoryList(postCategoryList);
        //게시물이 없어도 기본적으로 보여질 요소들

        List<PostDto> postList=this.getPostList(boardParamBundle);
        boardViewDto.setPostList(postList);//postCategory

        int totalCount=this.getPostCount(boardParamBundle);
        Paging paging= boardParamBundle.getPaging();
        paging.setTotalCount(totalCount);
        PageHandler pageHandler=new PageHandler(totalCount,paging.getPage(), paging.getPageSize(), paging.getListSize());
        boardViewDto.setPageHandler(pageHandler);
        FieldBundle fieldBundle=boardParamBundle.getFieldBundle();
        if(fieldBundle.getPostNo()!=null){
            Post post = Post.builder().boardNo(fieldBundle.getBoardNo()).postNo(fieldBundle.getPostNo()).build();
            post = getPost(post);
            if(post==null)
                return boardViewDto;
                //throw new NotFoundException();
//            this.increasePostHit(boardParamBundle);//조회수 증가

            PostCategory postCategory=PostCategory.builder()
                    .boardCategoryNo(boardDto.getBoardCategoryNo())
                    .postCategoryNo(post.getPostCategoryNo())
                    .build();
            postCategory=postCategoryDao.select(postCategory);
            PostDto postDto=new PostDto(post, postCategory.getCategoryName());
            boardViewDto.setPostDto(postDto);
            if (post.getCommentCount() > 0) {
                //게시물 조회, 댓글이 있을때
                Paging commentPaging= boardParamBundle.getCommentPaging();
                int commentPageSize=commentPaging.getPageSize();
                int commentPage=commentPaging.getPage();
                int commentTotalCount=post.getCommentCount();
                commentPaging.setTotalCount(commentTotalCount);
                if(commentPage==0) {
                    commentPage = CommentPageHandler.calcLastPage(commentTotalCount, commentPageSize);
                    commentPaging.setPage(commentPage);
                }
                commentPaging.setOffset((commentPage-1)*commentPageSize);
                Comment comment= Comment.builder()
                        .boardNo(fieldBundle.getBoardNo())
                        .postNo(fieldBundle.getPostNo())
//                        .boardNo(viewParamBundle.getBoardNo())
//                        .postNo(viewParamBundle.getPostNo())
                        .build();
                CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                        .comment(comment)
                        .usePage(true)
                        .paging(commentPaging)
                        .build();
//                CommentViewDto commentViewDto = commentService.getCommentList(commentParamDto);
                CommentViewDto commentViewDto = commentService.getCommentCTE(commentParamBundle);

                boardViewDto.setCommentViewDto(commentViewDto);
            }
        }
        return boardViewDto;
    }
    private boolean checkSearchQueryParam(BoardParamBundle boardParamBundle){
        Search search = boardParamBundle.getSearch();
        String query=search.getQuery();
        String word=search.getWord();
        if((query.equals("title content")||query.equals("title")||query.equals("content"))
            && word.length()>=2)
            return true;//검색 조건이 되는 경우
        return false;
    }
    @Override
    public List<PostDto> getPostList(BoardParamBundle boardParamBundle) {
//        QueryParamBundle queryParamBundle = ViewParamBundle.toQueryParamDto(viewParamBundle);
        List<PostDto> postList = (checkSearchQueryParam(boardParamBundle)) ?
                postDao.selectSearchedListInBoard(boardParamBundle)
                : postDao.selectList(boardParamBundle);
        return postList;
    }

    @Override
    public Post getPost(Post post) {
        return postDao.select(post);
        //게시물이 존재하지 않는 경우 예외처리
    }

    @Override
    public List<PostCategory> getPostCategoryList(int boardNo){
        int boardCategoryNo = boardDao.selectBoardCategoryNo(boardNo);
        List<PostCategory> postCategoryList = postCategoryDao.selectList(boardCategoryNo);
        return postCategoryList;
    }

    @Override
    public int uploadPost(Post post) throws SQLException {
        List<Image> images=moveImages(post);
        int rowCnt = postDao.insert(post);
        if (rowCnt == 0)
            throw new SQLException();
        if(images.size()>0){
            uploadImages(images, post.getPostNo());
        }
        return rowCnt;
    }
    @Transactional(rollbackFor = Exception.class)
    public void uploadImages(List<Image> imageList, int postNo) throws SQLException {
        for(Image image:imageList){
            image.setPostNo(postNo);
            int rowCnt=imageDao.insert(image);
            if(rowCnt==0)
                throw new SQLException();
        }
    }
    public void visitTree(Element element, Post post, String pathPrefix, String srcPrefix, List<Image> imageList){
        int boardNo=post.getBoardNo();
        if(element.tagName().equals("img")){
            String tempSrc=element.attr("src");
            String fileName=tempSrc.substring(tempSrc.indexOf(ImageDto.TEMP_DIRECTORY)+ImageDto.TEMP_DIRECTORY.length());
            File file=new File(ImageDto.TEMP_REAL_PATH+fileName);
            if(file.exists()) {
                String moved = pathPrefix+fileName;
                try {
                    FileUtils.moveFile(file, new File(moved));
                    String movedSrc =
//                            srcPrefix+fileName;
                            tempSrc.replace(ImageDto.TEMP_DIRECTORY, srcPrefix);
                    element.attr("src", movedSrc);

                    Image image=Image.builder().filename(fileName)
                            .originalFilename(element.attr("data-original-filename"))
                            .path(pathPrefix)
                            .boardNo(boardNo)
//                            .postNo(postNo)
                            .build();
                    imageList.add(image);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            Elements children = element.children();
            for(Element child:children){
                visitTree(child, post, pathPrefix, srcPrefix, imageList);
            }
        }
    }

//    @Transactional(rollbackFor = Exception.class)
    public List<Image> moveImages(Post post) throws SQLException {
        String content=post.getContent();
        List<Image> imageList=new ArrayList<>();
        Document doc = Jsoup.parseBodyFragment(content);
        int boardNo=post.getBoardNo();
        String srcPrefix=ImageDto.UPLOAD_DIRECTORY +boardNo+"/";
        String pathPrefix=ImageDto.UPLOAD_REAL_PATH +boardNo+"/";
        visitTree(doc.body(), post, pathPrefix, srcPrefix, imageList);
        String result=doc.body().children().toString();

        post.setContent(result);
        return imageList;
    }

    @Override
    public int editPost(Post post) throws SQLException {
//        String savePath=ImageDto.SAVE_DIRECTORY;
        List<String> erased=filterErasedImages(post);
        if(erased!=null && erased.size()>0) {
            eraseImageFiles(erased, post);
        }
        List<Image> images=moveImages(post);
        int rowCnt = postDao.update(post);
        if (rowCnt == 0)
            throw new SQLException();
        //이미지 있는 경우 추가
        if(images.size()>0){
            uploadImages(images, post.getPostNo());
        }
        return rowCnt;
    }

    private List<String> filterErasedImages(Post post) {
        String content=post.getContent();
        Image image=Image.builder()
                .boardNo(post.getBoardNo())
                .postNo(post.getPostNo())
                .build();

        Document doc = Jsoup.parseBodyFragment(content);
        Elements imgs = doc.body().getElementsByTag("img");
        List<String> savedFilenames=new ArrayList<>();
        String prefix=ImageDto.UPLOAD_DIRECTORY +post.getBoardNo()+"/";
        System.out.println(prefix);
        for(Element img:imgs){
            String imgSrc=img.attr("src");
            System.out.println("imgSrc = " + imgSrc);
//            if(imgSrc.startsWith(savePath)){
            if(imgSrc.contains(prefix)){
                savedFilenames.add(imgSrc.substring(imgSrc.indexOf(prefix)
                        +prefix.length()));
            }
        }
        System.out.println("savedFilenames : ");
//        if(savedFilenames.size()==0) return ;
        savedFilenames.stream().forEach(saved->System.out.printf("%s ", saved));
        
        List<String> filenames=imageDao.selectFilenameList(image);
        System.out.println("dbFilenames : ");
        filenames.stream().forEach(filename->System.out.printf("%s ", filename));
        List<String> erased=null;
        if(filenames.size()>0){
            erased=filenames.stream().filter(filename->!savedFilenames.contains(filename))
                    .collect(Collectors.toList());
        }
        System.out.println("erasedFilenames : ");
        if(erased!=null)
            erased.stream().forEach(e->System.out.printf("%s ", e));

        return erased;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int erasePost(Post post) throws SQLException {
        int rowCnt=0;
//        post=this.getPost(post);//가지고 있는 필드에 getLikeCount 정보가 없을 경우
//        int voteCount=post.getLikeCount()+post.getDislikeCount();
//        System.out.println("likeCount = " + voteCount);
//        int commentCount=post.getCommentCount();
//        if(commentCount>0){
//            rowCnt=commentDao.deleteList(post);
//            if(rowCnt!=commentCount)
//                throw new SQLTransactionRollbackException();
//        }
//        if(voteCount>0) {
//            rowCnt = postLikeDao.deleteList(post);
//            if (rowCnt != voteCount) throw new SQLTransactionRollbackException();
//        }
        eraseImages(post);

        rowCnt=postDao.delete(post);
        System.out.println("삭제된 게시글 수: "+rowCnt);
        if(rowCnt==0)
            throw new SQLException();
        return rowCnt;
    }

    public void eraseImages(Post post){
        Image img=Image.builder()
                .boardNo(post.getBoardNo())
                .postNo(post.getPostNo())
                .build();
//        List<Image> images=imageDao.selectList(img);
        List<String> filenames=imageDao.selectFilenameList(img);
        if(filenames!=null && filenames.size()>0)
            eraseImageFiles(filenames, post);
    }
    public void eraseImageFiles(List<String> filenames, Post post){

        ImageDto imageDto=new ImageDto();
        imageDto.setBoardNo(post.getBoardNo());
        imageDto.setPostNo(post.getPostNo());
        imageDto.setPath(ImageDto.UPLOAD_REAL_PATH +post.getBoardNo()+"/");
        imageDto.setFilenames(filenames);
        System.out.println("imageDto = " + imageDto);
        for(String filename: filenames) {
//            File file = new File(image.getPath() + image.getFilename());
            File file = new File(imageDto.getPath() + filename);
            if (file.exists()) {
                System.out.println("file.getName() 있음 = " + file.getName());
                boolean erased = file.delete();
                if (erased) {
                    System.out.println("파일 지워짐");
                }
            }
        }
        int rowCnt=imageDao.deleteList(imageDto);
    }

    @Override
    public Post votePost(PostLike postLike) throws SQLException, AlreadyVotedException {
        if(postLikeDao.selectExists(postLike))
            throw new AlreadyVotedException();//이미 추천 혹은 반대를 누른 경우 안내메시지 보내기
//            throw new SQLException();//이미 추천 혹은 반대를 누른 경우 안내메시지 보내기
        this.updatePostLike(postLike,"+");
        Post post = Post.builder()
                .boardNo(postLike.getBoardNo())
                .postNo(postLike.getPostNo())
                .build();
        return postDao.select(post);
    }

    @Override//추천 반대를 누른 후에는 버튼 onclick 메서드나 버튼 아이디 등을 바꿀 수 있도록
    public Post cancelVotePost(PostLike postLike) throws SQLException {
        postLike = postLikeDao.select(postLike);
        if(postLike==null)
//        if(!postLikeDao.selectExists(postLike))//취소하려는 좋아요/싫어요가 존재하지 않는 경우
            throw new SQLException();

        this.updatePostLike(postLike,"-");
        Post post = Post.builder()
                .boardNo(postLike.getBoardNo())
                .postNo(postLike.getPostNo())
                .build();
        return postDao.select(post);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePostLike(PostLike postLike, String sign) throws SQLException{
        int rowCnt=0;
        if(sign.equals("+")) {
            rowCnt = postLikeDao.insert(postLike);
        }else {
            rowCnt = postLikeDao.delete(postLike);
        }if(rowCnt==0)
            throw new SQLException();
        String field = Post.LIKE_COUNT_FIELD_NAME;
        if (!postLike.getIsLike()) {
            field = Post.DISLIKE_COUNT_FIELD_NAME;
        }
        SqlStr sqlStr=SqlStr.builder()
                .field(field)
                .sign(sign)
                .build();
        FieldBundle fieldBundle=FieldBundle.builder()
                .boardNo(postLike.getBoardNo())
                .postNo(postLike.getPostNo())
                .userNo(postLike.getUserNo())
                .build();
        BoardParamBundle boardParamBundle = BoardParamBundle.builder()
                .fieldBundle(fieldBundle)
                .sqlStr(sqlStr)
                .build();
        rowCnt = postDao.updateCountFieldInPost(boardParamBundle);
        if(rowCnt==0)
            throw new SQLException();
        return rowCnt;
    }
    @Override
    public int increasePostHit(Post post){
        FieldBundle fieldBundle=FieldBundle.builder()
                .boardNo(post.getBoardNo())
                .postNo(post.getPostNo())
                .build();
        SqlStr sqlStr=SqlStr.builder()
                .field(PostDto.HIT_FIELD_NAME)
                .sign("+")
                .build();
        BoardParamBundle boardParamBundle=BoardParamBundle.builder()
                .fieldBundle(fieldBundle)
                .sqlStr(sqlStr)
                .build();
        return postDao.updateCountFieldInPost(boardParamBundle);
    }
//    @Override
//    public int increasePostHit(BoardParamBundle boardParamBundle) {
//        HttpServletResponse response= boardParamBundle.getResponse();
//        String visitedList= boardParamBundle.getVisitedList();
//        FieldBundle fieldBundle=boardParamBundle.getFieldBundle();
//        int boardNo= fieldBundle.getBoardNo(), postNo= fieldBundle.getPostNo();
//
//        String view=new StringBuilder()
//                .append(boardNo)
//                .append('-').append(postNo)
//                .toString();
//
//        int rowCnt=0;
//        if(!visitedList.contains(view)){
//            visitedList = visitedList.equals("") ? view : visitedList+"/"+view;
//
//            Cookie cookie=new Cookie("vl", visitedList);
//            cookie.setPath("/");
//            cookie.setMaxAge(getRemainSecondForTommorow());
////            response.setLocale(Locale.KOREA);
//            response.addCookie(cookie);
//            SqlStr sqlStr=SqlStr.builder()
//                    .field(PostDto.HIT_FIELD_NAME)
//                    .sign("+")
//                    .build();
//            boardParamBundle.setSqlStr(sqlStr);
//            rowCnt=postDao.updateCountFieldInPost(boardParamBundle);
//        }
//        return rowCnt;
//    }
    
    //자정까지 남은 시간
//    private int getRemainSecondForTommorow() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime tommorow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
//        return (int) now.until(tommorow, ChronoUnit.SECONDS);
//    }
    @Override
    public List<BoardViewDto> getIndexView(Paging paging){
        List<BoardViewDto> boardViewDtoList=new ArrayList<>();
        List<BoardDto> boardList = boardDao.selectIndexBoards(1);
        BoardParamBundle boardParamBundle=BoardParamBundle.builder()
                .paging(paging)
                .build();
        for(BoardDto boardDto:boardList){
            BoardViewDto boardViewDto=new BoardViewDto();
            boardViewDto.setBoardDto(boardDto);

            FieldBundle fieldBundle=new FieldBundle();
            fieldBundle.setBoardNo(boardDto.getBoardNo());
            boardParamBundle.setFieldBundle(fieldBundle);
//            boardParamBundle.getFieldBundle().setBoardNo(boardDto.getBoardNo());
//            queryParamBundle.setBoardNo(boardDto.getBoardNo());
            List<PostDto> postList=postDao.selectList(boardParamBundle);
            boardViewDto.setPostList(postList);
            boardViewDtoList.add(boardViewDto);
        }
        return boardViewDtoList;
    }

    @Override
    public BoardViewDto getSearchResult(BoardParamBundle boardParamBundle) {
        Paging paging= boardParamBundle.getPaging();
//        Search search= boardParamBundle.getSearch();
//        QueryParamBundle queryParamBundle = QueryParamBundle.builder()
//                .paging(paging)
//                .search(search)
//                .build();
        List<PostDto> postDtoList = postDao.selectSearchedListAll(boardParamBundle);
        int totalCount=postDao.selectSearchedCountAll(boardParamBundle);
        PageHandler pageHandler=new PageHandler(totalCount, paging.getPage(), paging.getPageSize(), paging.getListSize());
        BoardViewDto boardViewDto=new BoardViewDto();
        boardViewDto.setPostList(postDtoList);
        boardViewDto.setPageHandler(pageHandler);
        return boardViewDto;
    }
}
