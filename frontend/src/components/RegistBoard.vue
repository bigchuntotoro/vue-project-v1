<template>
  <div>
    <!-- 팝업 내용 -->
    <el-dialog
      :title="ModalTitle"
      :visible.sync="openPopup"
      width="50%"
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
      <!-- 📎 [추가] 4. 파일 업로드 영역 -->
      <div style="margin-top: 20px; text-align: left">
        <span
          style="
            font-size: 14px;
            color: #606266;
            display: block;
            margin-bottom: 8px;
          "
          >첨부파일</span
        >
        <el-upload
          class="upload-demo"
          action=""
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :file-list="fileList"
          :limit="1"
          :on-exceed="handleExceed"
        >
          <el-button size="small" type="success" icon="el-icon-paperclip"
            >파일 선택</el-button
          >
          <div slot="tip" class="el-upload__tip">
            5MB 이하의 파일 1개만 업로드 가능합니다.
          </div>
        </el-upload>
      </div>
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
      id: null,
      title: "",
      writer: "",
      content: "",
      // 💡 파일 관련 상태 변수
      fileList: [],
      uploadFile: null, // 실제 백엔드로 보낼 File 객체
    };
  },

  // 💡 [해결] 빠져있던 computed 속성을 동등한 위치에 새로 넣어주었습니다.
  computed: {
    // 1. 팝업 제목 분기 처리 (기존에 에러가 났던 원인!)
    ModalTitle() {
      return this.editMode === true ? "글 수정" : "글 등록";
    },
    // 2. 혹시 다른 곳에서 사용 중일 수 있는 파라미터 빌더
    setParams() {
      return {
        ...(this.editMode && { id: this.id }),
        title: this.title,
        writer: this.writer,
        content: this.content,
      };
    },
  },

  methods: {
    // 💡 파일 선택 시 실행되는 이벤트
    handleFileChange(file, fileList) {
      this.fileList = fileList;
      this.uploadFile = file.raw; // 실제 File 객체 확보
    },
    // 💡 파일 삭제 시 실행되는 이벤트
    handleFileRemove(file, fileList) {
      this.fileList = fileList;
      this.uploadFile = null;
    },
    // 💡 파일 제한 초과 시 안내
    handleExceed() {
      this.$message.warning("파일은 최대 1개까지만 등록할 수 있습니다.");
    },

    registboard() {
      // 1. FormData 객체 생성
      const formData = new FormData();

      // 2. 일반 텍스트 데이터 추가
      formData.append("title", this.title);
      formData.append("writer", this.writer);
      formData.append("content", this.content);

      // 3. 첨부파일이 존재하는 경우에만 파일 추가
      if (this.uploadFile) {
        formData.append("file", this.uploadFile);
      }

      // 공통 통신 헤더 설정 (Multipart 전송 설정)
      const config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      };

      // 4. 등록/수정 분기 처리
      if (!this.editMode) {
        // [등록]
        axios
          .post("http://localhost:8080/api/boards", formData, config)
          .then((response) => {
            if (response) {
              this.successProcess("글이 성공적으로 등록되었습니다.");
            }
          })
          .catch((error) => {
            console.error("등록 실패:", error);
            this.$message.error("글 등록에 실패했습니다.");
          });
      } else {
        // [수정]
        formData.append("id", this.id); // 수정일 때는 id도 폼데이터에 추가

        // 백엔드 API 스펙이 PUT이면 PUT으로, POST이면 POST로 변경하세요.
        axios
          .put(`http://localhost:8080/api/boards/${this.id}`, formData, config)
          .then((response) => {
            if (response) {
              this.successProcess("글이 성공적으로 수정되었습니다.");
            }
          })
          .catch((error) => {
            console.error("수정 실패:", error);
            this.$message.error("글 수정에 실패했습니다.");
          });
      }
    },

    // 등록/수정 성공 시 공통 처리 로직
    successProcess(msg) {
      this.openPopup = false;
      this.clearForm();
      this.$emit("reload");
      this.$message({
        message: msg,
        type: "success",
      });
    },

    clearForm() {
      this.id = null;
      this.title = "";
      this.writer = "";
      this.content = "";
      this.editMode = false;
      // 💡 파일 데이터 초기화
      this.fileList = [];
      this.uploadFile = null;
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
