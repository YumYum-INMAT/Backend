package yumyum.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, 200, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_USERNAME(false,2020,"아이디를 입력해주세요."),
    POST_USERS_INVALID_USERNAME(false,2021,"잘못된 아이디 형식입니다."),

    POST_USERS_EMPTY_PASSWORD(false,2030,"비밀 번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false,2031,"비밀 번호는 특수문자 포함 8자 이상 20자리 이하입니다."),
    
    POST_USERS_EMPTY_NICKNAME(false,2040,"닉네임을 입력해주세요."),
    POST_USERS_INVALID_NICKNAME(false,2042,"닉네임은 한글 최소 2자, 최대 8자까지 사용 가능합니다."),
    
    POST_USERS_EMPTY_AGE(false,2050,"나이를 입력해주세요."),
    POST_USERS_INVALID_AGE(false,2051,"올바른 나이를 입력해주세요."),

    POST_USERS_EMPTY_GENDER(false,2060,"성별을 입력해주세요."),
    POST_USERS_INVALID_GENDER(false,2061,"올바른 성별을 입력해주세요."),

    POST_USERS_EMPTY_PHONENUMBER(false,2070,"휴대폰 번호를 입력해주세요."),
    POST_USERS_INVALID_PHONENUMBER(false,2071,"잘못된 휴대폰 번호입니다."),

    POST_USERS_EMPTY_PRIVACY(false,2080,"개인정보 약관 동의가 필요합니다."),
    POST_USERS_INVALID_PRIVACY(false,2081,"잘못된 개인정보 약관 동의입니다."),

    // [POST] /users/login
    POST_USERS_EMPTY_LOGIN_ID(false, 2090, "아이디를 입력해주세요."),
    POST_USERS_OVER_LENGTH_LOGIN_ID(false, 2091, "아이디는 3자리 이상 20자리 이하입니다."),

    
    // 신고 관련 요청 오류
    INVALID_REPORT_TYPE(false,2190,"잘못된 신고 형식입니다."),
    

    // 페이징 관련 요청 오류
    EMPTY_PAGE_INDEX(false,2200,"페이지 인덱스 값이 필요합니다."),
    INVALID_PAGE_INDEX(false,2201,"잘못된 페이지 인덱스입니다."),

    //게시글 관련 오류
    POST_EMPTY_TOPIC(false,2500,"제목을 입력해주세요."),
    POST_OVER_LENGTH_TOPIC(false,2505,"제목은 최대 45자까지 입력해주세요."),
    POST_EMPTY_CONTENTS(false,2510,"내용을 입력해주세요."),
    POST_OVER_LENGTH_CONTENTS(false,2515,"내용은 최대 255자까지 입력해주세요."),

    //댓글 관련 오류
    COMMENT_EMPTY_CONTENTS(false, 2600, "내용을 입력해주세요."),
    COMMENT_OVER_LENGTH_CONTENTS(false, 2605, "내용은 최대 100자까지 입력해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    FAILED_TO_LOGIN(false,3010,"없는 아이디이거나 비밀번호가 틀렸습니다."),
    
    NOT_ACTIVATED_USER(false,3015,"유효한 사용자가 아닙니다."),
    

    DUPLICATED_USERNAME(false,3030,"중복된 아이디입니다."),

    DUPLICATED_NICKNAME(false,3035,"중복된 닉네임입니다."),

    DUPLICATED_EMAIL(false,3040,"중복된 이메일입니다."),

    DUPLICATED_HEART(false,3045,"중복된 하트찜입니다."),

    ALREADY_HEART_CANCEL(false,3050,"이미 하트찜 해제 상태 입니다."),


    FAILED_TO_MODIFY_USERNAME(false,3040,"14일 이내에 이름 변경은 최대 2번입니다."),
    FAILED_TO_MODIFY_NICKNAME(false,3050,"14일 이내에 사용자 이름 변경은 최대 2번입니다."),


    ALREADY_PUBLIC_ACCOUNT(false,3060,"이미 공개 계정 상태입니다."),
    ALREADY_PRIVATE_ACCOUNT(false,3070,"이미 비공개 계정 상태입니다."),
    
    ALREADY_FOLLOW(false,3080,"이미 팔로우 상태입니다."),
    ALREADY_FOLLOW_REQUEST(false,3081,"이미 팔로우 요청 상태입니다."),

    ALREADY_UNFOLLOW(false,3090,"이미 언팔로우 상태입니다."),
    
    FAILED_TO_ACCEPT_FOLLOW(false,3100,"잘못된 팔로우 요청 승인입니다."),

    FAILED_TO_REJECT_FOLLOW(false,3110,"잘못된 팔로우 요청 취소입니다."),

    STILL_ENABLE_PRIVACY(false,3120,"이전의 개인정보 처리 방침 동의가 아직 유효합니다."),
    
    NEED_USER_PRIVACY(false,3130,"개인정보 처리 방침 동의가 필요합니다."),

    FAILED_TO_SEARCH_USER(false,3140,"해당 사용자를 찾을 수 없습니다."),

    FAILED_TO_SEARCH_POST(false,3150,"게시글을 찾을 수 없습니다."),
    ALREADY_POST_LIKE(false,3160,"이미 게시글 좋아요 상태입니다."),
    ALREADY_POST_UNLIKE(false,3165,"이미 게시글 좋아요 취소 상태입니다."),

    FAILED_TO_SEARCH_COMMENT(false,3170,"댓글을 찾을 수 없습니다."),
    ALREADY_COMMENT_LIKE(false,3180,"이미 댓글 좋아요 상태입니다."),
    ALREADY_COMMENT_UNLIKE(false,3185,"이미 댓글 좋아요 취소 상태입니다."),

    FAILED_TO_UPDATE_POST(false,3190,"게시글 수정에 실패하였습니다."),
    FAILED_TO_DELETE_POST(false,3200,"게시글 삭제에 실패하였습니다."),

    FAILED_TO_DELETE_COMMENT(false,3200,"댓글 삭제에 실패하였습니다."),

    FAILED_TO_REPORT_POST(false,3210,"게시글 신고에 실패하였습니다."),
    FAILED_TO_REPORT_COMMENT(false,3220,"댓글 신고에 실패하였습니다."),

    INVALID_KAKAO_USER(false,3500,"잘못된 카카오 로그인입니다."),

    //댓글 관련 오류
    FAILED_TO_UPDATE_COMMENT(false,3510,"댓글 수정에 실패하였습니다."),

    COMMENT_LIKE_EMPTY(false,3600,"댓글 좋아요를 먼저 해야합니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    WRONG_DATE(false, 4032, "잘못된 날짜 형식입니다."),


    SEND_SMS_ERROR(false, 5000, "문자 인증 전송을 실패하였습니다."),
    UNEXPECTED_ERROR(false, 5013, "예상치 못한 에러가 발생했습니다. 다시 시도해주세요.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
