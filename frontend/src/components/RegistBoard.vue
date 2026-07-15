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
      id: null, // 👈 bno 대신 id로 변경
      title: "",
      writer: "",
      content: "",
    };
  },
  computed: {
    setParams() {
      const params = {
        // 💡 수정 모드일 때만 파라미터에 id를 포함하도록 설정
        ...(this.editMode && { id: this.id }),
        title: this.title,
        writer: this.writer,
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
        // ... 기존 등록 코드 그대로 유지 ...
      }
      // 2. 글 수정 모드
      else {
        // 💡 백엔드 URL 스펙에 맞춰 A 방식(PUT /api/boards/{id})
        // 또는 B 방식(POST /api/boards) 중 하나를 선택하세요.

        // [방식 A] 표준적인 REST API 수정 방식 (PUT + URL 패스에 ID 포함)
        axios
          .put(`http://localhost:8080/api/boards/${this.id}`, this.setParams)
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
    clearForm() {
      this.id = null; // 👈 id 초기화
      this.title = "";
      this.writer = "";
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
