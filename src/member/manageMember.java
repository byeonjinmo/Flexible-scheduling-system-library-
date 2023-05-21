/*
 * @ 해결과제 :
 * 
 * 1. allLockers, occupiedLockers를 따로 분리해서 저장 -> (일단 싱글톤 객체, 나중되면 서버에 저장)
 * 2. locker를 main에서 일일히 만들지 말고, 미리 만들어둔 KBU 사물함 목록이 있으면 좋겠다.
 * 
*/
package member;

import member.model.Member;
import locker.model.Locker;
import locker.LockerList;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class manageMember<T> {
    private Member member;

    private boolean loggedIn = false;
    private int loginAttempts;
    private Map<String, String> userCredentials; // 사용자 계정 정보 저장

    private static List<Locker> allLockers = LockerList.getAllLockers(); // 전체 locker 목록 (싱글톤 객체 생성)
    private static HashMap<Integer, Locker> occupiedLockers = LockerList.getOccupiedLockers(); // 예약된 locker 목록 (싱글톤 객체 생성)

    public manageMember() {
        userCredentials = new HashMap<>();
    }

    public void setMember(Member object) {
        this.member = object;
        System.out.println(member.getter());
    }

    /*로그인 기능 구현*/
    // 회원가입
    public int signUp(Member member) {  //클래스 타입으로 username, password를 받아 유연하게 회원가입
        if (loggedIn) {
            System.out.println("로그인 상태에서는 회원가입이 불가능합니다.");
            return -1;
        }
            String username = member.getUsername();
            String password = member.getPassword();

            if (userCredentials.containsKey(username)) {
                System.out.println("이미 존재하는 사용자명입니다. 다시 정보 입력해주세요.");
                return -1;
            }

            userCredentials.put(username, password);
            System.out.println("회원가입이 완료되었습니다.");
            return 1;
        }

    public int login(String username, String password) {


            if (loggedIn) {
                System.out.println("이미 로그인되어 있습니다.");
                return -1;
            }

            if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                loggedIn = true;
                System.out.println("로그인 성공!");
                return 1;
            } if(member.getUsername() != username || member.getPassword() != password){
                loggedIn = false;
                System.out.println("로그인 실패!");
                return -1;
            }

        return 0;
    }

    // 로그아웃
    public void logout() {
        loggedIn = false;
        System.out.println("로그아웃 되었습니다.");
    }

    // 로그인 상태 체크
    public boolean login_check() {
        return loggedIn;
    }


    /* 예약 기능 구현 */
    // 빈 사물함 목록 출력
    public List<Locker> check_unoccupiedLocker() {
        List<Locker> unoccupied = new ArrayList<>();

        if(occupiedLockers.size() == 0) { // 예약된 locker가 없으면, 전체 사물함 리턴
            return allLockers;
        }
        
        for(Locker locker : allLockers) { // 전체 locker를 순회하며, (예약된 locker제외) unoccupied 목록에 추가
            if(!occupiedLockers.containsValue(locker))
                unoccupied.add(locker);
        } 
        return unoccupied;
    }

    // 예약 : 예약된 사물함 목록에 [키=pk, 값=locker] 저장  (pk에 학번 넣는게 바람직)
    public void reserve_locker(int pk, Locker locker) {
        if(!loggedIn) {
            System.out.println("로그인이 필요합니다.");
            return;
        }
        if(occupiedLockers.containsKey(pk)) {
            System.out.println("이미 다른 사물함을 예약중입니다.");
            return;
        }
        if(occupiedLockers.containsValue(locker)) {
            System.out.println("예약된 사물함입니다.");
            return;
        }
        
        occupiedLockers.put(pk, locker); // Hashmap에 [pk, locker] 저장
        System.out.printf("%s-%d 사물함이 예약되었습니다.\n", locker.getbuildingName(), locker.getLockNum());
    }

    // 예약취소 : 예약된 사물함 목록에서 키=pk에 해당하는 요소 제거
    public void cancel_locker(int pk) {
        if(!loggedIn) {
            System.out.println("로그인이 필요합니다.");
            return;
        }
        if(!occupiedLockers.containsKey(pk)) {
            System.out.println("예약된 사물함이 없습니다.");
            return;
        }
        
        occupiedLockers.remove(pk);
        System.out.println("사물함이 예약취소 되었습니다.");
    }

    // 예약확인 : 사용중인 사물함 목록에서 키=pk에 해당하는 Locker객체 반환 +정보 출력  (void로 출력만 해도??)
    public Locker check_locker(int pk) {
        Locker myLocker = occupiedLockers.get(pk);
        
        if(!loggedIn) {
            System.out.println("로그인이 필요합니다.");
            return null;
        }
        if(myLocker == null) {
            System.out.println("예약된 사물함이 없습니다.");
            return null;
        }

        System.out.printf("%s-%d 사물함을 예약중입니다.\n", myLocker.getbuildingName(), myLocker.getLockNum());
        return myLocker;
    }
}


