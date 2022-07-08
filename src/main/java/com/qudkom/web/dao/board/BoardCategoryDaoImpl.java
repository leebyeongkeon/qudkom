package com.qudkom.web.dao.board;

import com.qudkom.web.domain.vo.board.BoardCategory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardCategoryDaoImpl implements BoardCategoryDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.board.BoardCategoryMapper.";

    @Override
    public List<BoardCategory> selectBoardCategoryList(int status) {
//        int status=0;
        return session.selectList(namespace+"selectList",status);
    }

    @Override
    public BoardCategory select(int boardCategoryNo) {
        return session.selectOne(namespace+"select",boardCategoryNo);
    }
    /*
    public List<BoardCategory> selectBoardCategoryList(){
//        Map<Integer, BoardCategory> map=new TreeMap<>();
        List<BoardCategory> list=new ArrayList<>();
        String sql="";
        try {
            Class.forName("");
            Connection conn= DriverManager.getConnection("","","");
            PreparedStatement pstmt=conn.prepareStatement(sql);
            ResultSet rs=pstmt.executeQuery();
            while(rs.next()){
                BoardCategory boardCategory=new BoardCategory(rs.getInt(""),
                        rs.getString(""));
                list.add(boardCategory);
//                map.put(boardCategory.getBoardCategoryNo(), boardCategory);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
     */
}
