<template>
  <div>
    <!-- 글 상세 보기 내용 -->
    <el-dialog
      title="글 상세 보기"
      :visible.sync="openDetail"
      width="50%"
      center
    >
      <div class="detail-container">
        <p><strong>글 번호 :</strong> {{ boardDetail.bno }}</p>
        <p><strong>제목 :</strong> {{ boardDetail.title }}</p>
        <p>
          <strong>등록일 :</strong>
          {{ boardDetail.regDate || boardDetail.createdDate }}
        </p>
        <p><strong>작성자 :</strong> {{ boardDetail.writer }}</p>
        <p><strong>내용 :</strong></p>
        <div class="content-box">
          {{ boardDetail.content }}
        </div>

        <!-- 🖼️ [추가] 첨부파일이 이미지(그림) 형식일 때만 보여주는 영역 -->
        <div v-if="isImageFile" class="image-preview-zone">
          <img :src="imageUrl" alt="첨부 이미지" class="attached-image" />
        </div>

        <!-- 📎 [추가] 첨부파일 다운로드 영역 (이미지 유무 상관없이 파일이 존재하면 노출) -->
        <div v-if="boardDetail.originFileName" class="file-download-zone">
          <i class="el-icon-paperclip"></i>
          <strong>첨부파일 :</strong>
          <a
            :href="imageUrl"
            :download="boardDetail.originFileName"
            class="file-link"
          >
            {{ boardDetail.originFileName }} (클릭 시 다운로드)
          </a>
        </div>
      </div>

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
      boardDetail: {}, // 기존 배열([])을 상세 객체인 빈 중괄호({})로 수정
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

    // 💡 [추가] 첨부파일이 이미지 형식(jpg, jpeg, png, gif, webp)인지 체크
    isImageFile() {
      if (!this.boardDetail || !this.boardDetail.storeFileName) return false;

      const allowedExtensions = ["jpg", "jpeg", "png", "gif", "webp"];
      // 파일명에서 확장자만 추출
      const extension = this.boardDetail.storeFileName
        .split(".")
        .pop()
        .toLowerCase();
      return allowedExtensions.includes(extension);
    },

    // 💡 [추가] 백엔드로부터 이미지 파일을 조회/스트리밍하는 URL 매핑
    imageUrl() {
      if (!this.boardDetail || !this.boardDetail.storeFileName) return "";
      // 백엔드 이미지 스트리밍 API 주소 연결
      return `http://localhost:8080/api/images/${this.boardDetail.storeFileName}`;
    },
  },
  methods: {
    getBoardDetail(row) {
      const boardId = row.id || row.bno;

      console.log("상세조회 요청 ID:", boardId);
      console.log("목록에서 전달받은 row 전체 데이터:", row);

      axios
        .get(`http://localhost:8080/api/boards/${boardId}`)
        .then((response) => {
          console.log("서버 응답 데이터:", response.data);

          if (response.data) {
            this.boardDetail = response.data;

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

      this.$refs.registPopup.id = this.boardDetail.id || this.boardDetail.bno;
      this.$refs.registPopup.writer = this.boardDetail.writer;
      this.$refs.registPopup.title = this.boardDetail.title;
      this.$refs.registPopup.content = this.boardDetail.content;
      this.$refs.registPopup.editMode = true;
      this.$refs.registPopup.openPopup = true;
    },
    clcikDeleteButton() {
      const boardId = this.boardDetail.id || this.boardDetail.bno;

      if (!boardId) {
        this.$message.error("글 번호를 찾을 수 없습니다.");
        return;
      }

      axios
        .delete(`http://localhost:8080/api/boards/${boardId}`)
        .then((response) => {
          if (response) {
            this.openDetail = false;
            this.reload();
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
      this.$emit("reload");
    },
  },
};
</script>

<style scoped>
.detail-container {
  line-height: 1.8;
  font-size: 14px;
}

.content-box {
  background-color: #fcfcfc;
  border: 1px solid #ebeef5;
  padding: 15px;
  border-radius: 4px;
  min-height: 100px;
  white-space: pre-wrap; /* 줄바꿈 유지 */
  margin-bottom: 20px;
}

/* 🖼️ 이미지 시각 영역 스타일 */
.image-preview-zone {
  text-align: center;
  margin: 20px 0;
  background-color: #fafafa;
  border: 1px dashed #dcdfe6;
  padding: 15px;
  border-radius: 6px;
}

.attached-image {
  max-width: 100%; /* 부모 박스를 넘지 않게 조정 */
  max-height: 400px; /* 이미지 높이가 너무 높을 시 스크롤 가두기용 */
  object-fit: contain;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 📎 다운로드 링크 영역 스타일 */
.file-download-zone {
  background-color: #f4f4f5;
  padding: 10px 15px;
  border-radius: 4px;
  margin-top: 15px;
  display: flex;
  align-items: center;
}

.file-link {
  margin-left: 8px;
  color: #409eff;
  text-decoration: none;
  font-weight: bold;
}

.file-link:hover {
  text-decoration: underline;
  color: #66b1ff;
}
</style>
