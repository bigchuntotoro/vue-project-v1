<template>
  <div>
    <!-- 글 상세 보기 내용 -->
    <el-dialog
      title="글 상세 보기"
      :visible.sync="openDetail"
      width="30%"
      center
    >
      글 번호 : {{ boardDetail.bno }} <br />
      제목 : {{ boardDetail.title }} <br />
      등록일 : {{ boardDetail.regDate }} <br />
      내용 : {{ boardDetail.content }} <br />
      작성자 : {{ boardDetail.writer }} <br />
      <el-row type="flex" justify="end" style="margin-top: 30px">
        <el-button type="primary" @click="clcikEditButton()">수정</el-button>
        <el-button type="danger" @click="clcikDeleteButton()">삭제</el-button>
      </el-row>
    </el-dialog>
    <regist-board @reload="reload" ref="registPopup"></regist-board>
  </div>
</template>

<script>
import axios from "axios";
import RegistBoard from "./RegistBoard.vue";

export default {
  name: "BoardDetail",
  components: { RegistBoard },
  data() {
    return {
      openDetail: false,
      boardDetail: [],
    };
  },
  computed: {
    setParams() {
      const params = {
        title: this.title,
        content: this.content,
      };
      return params;
    },
  },
  methods: {
    getBoardDetail(row) {
      // 1. 목록에서 넘어온 row 데이터 중 글 번호(id 또는 bno)를 확보합니다.
      // 백엔드 필드명 스펙에 맞춰 row.id 또는 row.bno 중 매핑되는 것을 사용합니다.
      const boardId = row.id || row.bno;

      // 디버깅용 로그: 실제로 어떤 ID 값이 추출되었는지 브라우저 콘솔에서 확인 가능합니다.
      console.log("상세조회 요청 ID:", boardId);
      console.log("목록에서 전달받은 row 전체 데이터:", row);

      // 2. 백엔드 @GetMapping("/{id}") 구조에 맞게 URL 패스에 ID를 붙여 GET 요청을 보냅니다.
      axios
        .get(`http://localhost:8080/api/boards/${boardId}`)
        .then((response) => {
          console.log("서버 응답 데이터:", response.data);

          // 3. 백엔드가 Board 객체를 통째로 바로 리턴하므로 response.data를 바로 매핑합니다.
          if (response.data) {
            this.boardDetail = response.data;

            // 만약 기존 el-dialog 템플릿 코드에서 {{ boardDetail.bno }} 구조를 쓰고 있다면
            // 백엔드 변수명(id)과 맞춰주기 위해 안전하게 bno 필드도 채워줍니다.
            if (!this.boardDetail.bno && this.boardDetail.id) {
              this.boardDetail.bno = this.boardDetail.id;
            }

            this.openDetail = true; // 모달 팝업 열기
          }
        })
        .catch((error) => {
          console.error("상세보기 로드 실패:", error);
        });
    },
    clcikEditButton() {
      this.openDetail = false;

      // 💡 registPopup.bno 대신 registPopup.id 에 값을 할당합니다.
      this.$refs.registPopup.id = this.boardDetail.id || this.boardDetail.bno;
      this.$refs.registPopup.writer = this.boardDetail.writer;
      this.$refs.registPopup.title = this.boardDetail.title;
      this.$refs.registPopup.content = this.boardDetail.content;
      this.$refs.registPopup.editMode = true;
      this.$refs.registPopup.openPopup = true;
    },
    clcikDeleteButton() {
      // boardDetail에 들어있는 id 값을 확실하게 가져옵니다.
      const boardId = this.boardDetail.id || this.boardDetail.bno;

      if (!boardId) {
        this.$message.error("글 번호를 찾을 수 없습니다.");
        return;
      }

      // 💡 메서드를 delete로 바꾸고 URL 뒤에 id를 붙여서 요청합니다.
      axios
        .delete(`http://localhost:8080/api/boards/${boardId}`)
        .then((response) => {
          // 백엔드 리턴 방식에 따라 response 조건은 조정 가능 (예: response.data 성공 여부 등)
          if (response) {
            this.openDetail = false; // 상세보기 팝업 닫기
            this.reload(); // 목록 새로고침
            this.$message({
              message: "글이 성공적으로 삭제되었습니다.",
              type: "success",
            });
          }
        })
        .catch((error) => {
          console.error("삭제 실패:", error);
          this.$message.error("글 삭제에 실패했습니다.");
        });
    },
    reload() {
      // 목록을 재로딩을 위해 이벤트 emit
      this.$emit("reload");
    },
  },
};
</script>

<style scoped></style>
