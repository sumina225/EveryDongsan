<template>
    <div class="container">
        <!-- 로고 -->
        <div class="logo" @click="goToHome">
            <img src="@/assets/mainlog.png" alt="로고"/>
        </div>

        <!-- 로그인 폼 -->
        <form @submit.prevent="handleLogin">
            <div class="input-group">
                <input v-model="username" type="text" placeholder="아이디" required="required"/>
            </div>
            <div class="input-group">
                <input
                    v-model="password"
                    type="password"
                    placeholder="비밀번호"
                    required="required"/>
            </div>
            <div class="input-group">
                <button :disabled="isLoading" type="submit" class="login-btn">
                    {{ isLoading ? "로그인 중..." : "로그인" }}
                </button>
            </div>

            <!-- 로그인 유지 체크박스 -->
            <div class="login-options">
                <label class="keep-login">
                    <input type="checkbox" v-model="isChecked" class="checkbox-input"/>
                    <span class="checkbox"></span>
                    아이디 저장
                </label>
                <router-link to="/find" class="forgot-password">
                    아이디/비밀번호 찾기
                </router-link>
            </div>

            <!-- 회원가입 링크 -->
            <div class="signup">
                <p>
                    <router-link to="/sign">회원가입</router-link>
                </p>
            </div>
        </form>

        <!-- 하단 링크 -->
        <footer>
            <a href="https://everytime.kr/page/serviceagreement">이용약관</a>
            <a href="https://everytime.kr/page/privacy">개인정보처리방침</a>
            <a href="https://everytime.kr/page/faq">문의하기</a>
            <a href="https://everytime.kr" class="copyright">© 에브리동산</a>
        </footer>
    </div>
</template>

<script setup="setup">
    import {ref, onMounted} from "vue";
    import {useRouter} from "vue-router";
    import {useUserStore} from "@/stores/useUserStore";
    import Swal from "sweetalert2";

    const username = ref(""); // 아이디
    const password = ref(""); // 비밀번호
    const isChecked = ref(false); // 아이디 저장 여부
    const isLoading = ref(false); // 로딩 상태
    const router = useRouter();
    const userStore = useUserStore();

    // 페이지 로드 시 저장된 아이디 불러오기
    onMounted(() => {
        const savedUsername = localStorage.getItem("savedUsername");
        if (savedUsername) {
            username.value = savedUsername; // 저장된 아이디를 입력 필드에 표시
            isChecked.value = true; // 체크박스 상태를 저장된 상태로 설정
        }
    });

    const handleLogin = async () => {
        if (!username.value.trim() || !password.value.trim()) {
            Swal.fire({icon: "error", title: "입력 오류", text: "아이디와 비밀번호를 입력해주세요."});
            return;
        }

        isLoading.value = true;

        try {
            const credentials = {
                id: username.value,
                password: password.value
            };
            console.log("전송 데이터:", credentials);

            const success = await userStore.login(credentials);

            if (success) {
                // 아이디 저장 처리
                if (isChecked.value) {
                    localStorage.setItem("savedUsername", username.value); // 아이디 저장
                } else {
                    localStorage.removeItem("savedUsername"); // 아이디 삭제
                }

                Swal
                    .fire(
                        {icon: "success", title: "로그인 성공", text: `환영합니다, ${userStore.user.name}님!`, confirmButtonText: "확인"}
                    )
                    .then(() => {
                        router.push("/"); // 메인 페이지로 이동
                    });
            } else {
                Swal.fire({icon: "error", title: "로그인 실패", text: "아이디와 비밀번호를 확인해주세요."});
            }
        } catch (error) {
            console.error("로그인 처리 중 오류 발생:", error.message);
            Swal.fire({icon: "error", title: "오류", text: "로그인 처리 중 문제가 발생했습니다."});
        } finally {
            isLoading.value = false;
        }
    };

    const goToHome = () => {
        router.push("/");
    };
</script>

<style scoped="scoped">
    * {
        margin: 0;
        padding: 0;
        border: 0;
        -webkit-touch-callout: none;
    }

    address,
    article,
    aside,
    body,
    dd,
    div,
    dl,
    dt,
    form,
    h1,
    h2,
    h3,
    h4,
    input,
    li,
    nav,
    ol,
    p,
    pre,
    select,
    span,
    td,
    textarea,
    th,
    ul {
        font-family: "Apple SD Gothic Neo", "맑은 고딕", "돋움", sans-serif;
    }

    img {
        display: block;
    }

    a {
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0.05);
        color: inherit;
        text-decoration: none;
    }

    @media (prefers-color-scheme: dark) {
        a {
            -webkit-tap-highlight-color: rgba(255, 255, 255, 0.05);
        }
    }

    address,
    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
        font-weight: normal;
    }

    input,
    select,
    textarea {
        outline: none;
    }

    input,
    textarea {
        caret-color:rgb(79, 168, 27);;
    }

    @media (prefers-color-scheme: dark) {
        input,
        textarea {
            caret-color:rgb(79, 168, 27);;
        }
    }

    input::-moz-placeholder,
    textarea::-moz-placeholder {
        color: #a6a6a6;
    }

    @media (prefers-color-scheme: dark) {
        input::-moz-placeholder,
        textarea::-moz-placeholder {
            color: #6e6e6e;
        }
    }

    input::placeholder,
    textarea::placeholder {
        color: #a6a6a6;
    }

    @media (prefers-color-scheme: dark) {
        input::placeholder,
        textarea::placeholder {
            color: #6e6e6e;
        }
    }

    body {
        color: #292929;
        background-color: #ffffff;
        font-size: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }

    .container {
        position: absolute;
        top: 50%;
        left: 50%;
        width: 360px;
        transform: translate(-50%, -50%);
    }

    .logo img {
        width: 300px;
        /* 원하는 너비 */
        height: auto;
        /* 이미지 비율 유지 */
        display: block;
        margin: 0 auto;
    }

    .input-group {
        margin-top: 20px;
    }

    input {
        display: block;
        box-sizing: border-box;
        padding: 10px;
        border-radius: 12px;
        width: 100%;
        font-size: 16px;
        background-color: #f2f2f2;
    }

    button {
        display: block;
        box-sizing: border-box;
        padding: 10px;
        border-radius: 12px;
        width: 100%;
        font-size: 16px;
        font-weight: bold;
        background-color: rgb(79, 168, 27);
        /* 활성화 상태 배경색 */
        ;
        color: white;
        cursor: pointer;
    }

    button:hover {
        background-color: rgb(31, 81, 2);;
    }

    .login-options {
        display: flex;
        justify-content: space-between;
        margin-top: 16px;
    }

    .keep-login {
        display: inline-flex;
        align-items: center;
        color: #a6a6a6;
        font-size: 14px;
        cursor: pointer;
        gap: 8px;
    }

    .checkbox-input {
        display: none;
    }

    .checkbox {
        width: 14px;
        height: 14px;
        border: 1px solid #a6a6a6;
        border-radius: 3px;
        background-color: white;
        position: relative;
        cursor: pointer;
    }

    .checkbox-input:checked + .checkbox {
        background-color: rgb(79, 168, 27);;
        border-color: rgb(79, 168, 27);;
    }

    .checkbox:after {
        content: "✔";
        position: absolute;
        top: 0;
        left: 2px;
        font-size: 12px;
        color: white;
        opacity: 0;
        transition: opacity 0.2s;
    }

    .checkbox-input:checked + .checkbox:after {
        opacity: 1;
    }

    .forgot-password {
        color: #737373;
        font-size: 14px;
        text-decoration: underline;
    }

    .signup {
        text-align: center;
        margin-top: 40px;
    }

    .signup a {
        color: #737373;
        font-size: 14px;
        text-decoration: underline;
    }

    footer {
        display: flex;
        justify-content: center;
        gap: 16px;
        margin-top: 96px;
    }

    footer a {
        color: #737373;
        font-size: 14px;
    }

    footer a.copyright {
        font-weight: bold;
    }
</style>