package sec02.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

	
	private Connection conn;

	private PreparedStatement pstmt;

	
	private DataSource dataFactory;
	
	//생
	
	public MemberDAO() {
		System.out.println("MemberDAO 객체 생성");
		
		try {
			//1. 연결을 하기 위한 컨텍스트(pro07) 인식을 위한 Context객체
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
//			envContext.lookup("jdbc/oracle");
			dataFactory=(DataSource)envContext.lookup("jdbc/oracle");
			//이 코드는 JNDI를 사용하여 "java:/comp/env" 컨텍스트에서 "jdbc/oracle"라는 이름으로 등록된
			//데이터 소스를 얻어와서 dataFactory 변수에 할당하는 것입니다.
			//이렇게 얻어온 데이터 소스를 사용하여 데이터베이스 연결을 설정하고 관리할 수 있습니다.
			
			
		}catch(Exception e) {
			System.out.println("MemberDAO 객체에서 DB 연결 관련 에러");
		}
		
		
	}
	
	//메
	
	// 연결 후 회원 목록을 가져오라는 메소드 (서블릿에서 실행되고 연결된 곳)
	public List<MemberBean> listMembers(){
		
		
		List<MemberBean> list = new ArrayList<MemberBean>();	//회원들의 정보를 기록하기 위한 List컬렉션
		
		try {
			
			
			
//		connDB();
			//2. DB연결 우선
			conn=dataFactory.getConnection();
		
		//3. 연결 객체가(conn) sql을 돌려야 함, sql을 돌리기 위해서는 sql 관련 문구를 처리하는 PreparedStatement Interface사용.
		
		//4. SQL 작성
		String query = "select * from T_MEMBER";	//테이블, 가져올 데이터
		System.out.println("실행한 sql : " + query);
//		ResultSet rs = stmt.executeQuery(query);	//ResultSet : DB에서 가져온 데이터를 읽음	
		//executeQuery : Executes the given SQL statement, which returns a single ResultSet object. (하나의 ResultSet객체를 반환하는 SQL statement를 받아 실행)
		
		pstmt=conn.prepareStatement(query);
		
		ResultSet rs = pstmt.executeQuery();
		
		//rs.next() : Moves the cursor forward one row from its current position
		//테이블 첫째 행부터 한 행씩 다음행으로 이동
		
		while(rs.next()) {	//참고할 행이 있는한 반복
			// 결과테이블(ResultSet)의 칼럼 인식 후 결과값 가져오기
			String id=rs.getString("id");
			System.out.println("나온 id " + id);
			String pwd=rs.getString("pwd");
			String name=rs.getString("name");
			String email=rs.getString("email");
			Date joindate = rs.getDate("joindate");	//getdate는 Date클래스로 타입맞추기
			
			//MemberVO 객체를 만들어서 그 객체에 ResultSet에서 나온 결과를 set해야함 //"MemberVo 객체 필드에 값을 지정 -> list에 저장" 반복
			MemberBean vo = new MemberBean();
			vo.setId(id);
			vo.setPwd(pwd);
			vo.setName(name);
			vo.setEmail(email);
			vo.setJoinDate(joindate);
			
			list.add(vo);
			
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		}catch(Exception e){
			System.out.println("연결시 에러");
		}
		
		return list;	//DB에서 가져온 데이터들을 list에 저장 후 반환 및 메소드가 끝났으므로 다시 서블릿으로 이동
	}
	
	
	//회원을 추가 메소드
	public void addMember(MemberBean m) {
		try {
			conn=dataFactory.getConnection();
			
			String id=m.getId();
			String pwd = m.getPwd();
			String name = m.getName();
			String email = m.getEmail();
			System.out.println(id+pwd+name+email);
			
			String query="insert into t_member(id,pwd,name,email) VALUES(?,?,?,?)";
			System.out.println("회원 추가 sql문 : " + query);
			
			pstmt=conn.prepareStatement(query);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			
			pstmt.executeUpdate();  // 추가시 executeUpdate
			
			pstmt.close();
	
			
		} catch (Exception e) {
			System.out.println("회원추가시 에러");
		}
	}

	//회원 삭제 코드
		public void delMember(String id) {
			System.out.println("삭제하고자 하는 id + "  +  id);
			try {
				conn = dataFactory.getConnection();
				
				String query = "delete from t_member" + " where id=?";
				System.out.println("prepareStatememt:" + query);
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, id);
				pstmt.executeUpdate();
				pstmt.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
}
