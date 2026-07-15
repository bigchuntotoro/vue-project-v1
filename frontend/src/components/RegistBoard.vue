<template>
  <div>
    <!-- 팝업 내용 -->
    <el-dialog
      :title="ModalTitle"
      :visible.sync="openPopup"
      width="30%"
      center
      :close-on-click-modal="false"
    >
      <!-- 본문 영역 -->
      <!-- 1. 제목 입력 -->
      <el-input placeholder="제목을 입력해 주세요" v-model="title"></el-input>

      <!-- [추가] 2. 작성자 입력란 -->
      <el-input
        style="margin-top: 20px"
        placeholder="작성자를 입력해 주세요"
        v-model="writer"
      ></el-input>

      <!-- 3. 내용 입력 -->
      <el-input
        style="margin-top: 20px"
        type="textarea"
        :rows="12"
        placeholder="내용을 입력해 주세요."
        v-model="content"
      >
      </el-input>

      <!-- dialog footer 영역 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="registboard()">확인</el-button>
        <el-button @click="openPopup = false">취소</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "RegistBoard",
  data() {
    return {
      openPopup: false,
      editMode: false,
      title: "",
      writer: "", // [추가] 작성자 상태 변수
      content: "",
    };
  },
  computed: {
    // 전송할 데이터를 하나의 객체로 묶어줍니다.
    setParams() {
      const params = {
        title: this.title,
        writer: this.writer, // [추가] 전송 파라미터에 작성자 추가
        content: this.content,
      };
      return params;
    },
    ModalTitle() {
      return this.editMode === true ? "글 수정" : "글 등록";
    },
  },
  methods: {
    registboard() {
      // 1. 글 등록 모드
      if (!this.editMode) {
        const regData = this.setParams;

        console.log("서버로 보내는 등록 데이터:", regData);

        axios
          .post("http://localhost:8080/api/boards", regData)
          .then((response) => {
            // Spring Boot에서 등록 성공 시 ID(Long)를 반환하므로, response가 존재하면 성공 처리합니다.
            if (response) {
              this.openPopup = false;
              this.clearForm();
              this.$emit("reload"); // 목록 화면 리로드 이벤트 발생
              this.$message({
                message: "글이 성공적으로 등록되었습니다.",
                type: "success",
              });
            }
          })
          .catch((error) => {
            console.error(
              "등록 실패 상세 이유:",
              error.response ? error.response.data : error,
            );
            this.$message.error(
              "글 등록에 실패했습니다. 입력값을 확인해 주세요.",
            );
          });
      }
      // 2. 글 수정 모드
      else {
        axios
          .post("http://localhost:8080/api/boards", this.setParams)
          .then((response) => {
            if (response) {
              this.openPopup = false;
              this.clearForm();
              this.$emit("reload");
              this.$message({
                message: "글이 성공적으로 수정되었습니다.",
                type: "success",
              });
            }
          })
          .catch((error) => {
            console.error(
              "수정 실패 상세 이유:",
              error.response ? error.response.data : error,
            );
            this.$message.error("글 수정에 실패했습니다.");
          });
      }
    },

    // 동작 성공 후 또는 팝업이 닫힐 때 입력 필드를 청소하는 메서드
    clearForm() {
      this.title = "";
      this.writer = ""; // [추가] 작성자 초기화
      this.content = "";
      this.editMode = false;
    },
  },
};
</script>

<style scoped>
/* 입력란 간의 간격을 조절하여 팝업 레이아웃을 더 깔끔하게 다듬습니다. */
.el-input {
  margin-bottom: 5px;
}
</style>
