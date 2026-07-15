<template>
  <div class="board-container">
    <!-- 상단 헤더 영역 (타이틀 & 스타일리시 글쓰기 버튼) -->
    <div class="board-header">
      <div class="title-area">
        <span class="sub-title">OVERVIEW</span>
        <h2 class="main-title">📋 게시판 목록</h2>
      </div>

      <!-- [추가] 우측 정렬된 세련된 글쓰기 버튼 -->
      <div class="action-area">
        <el-button
          type="primary"
          icon="el-icon-edit"
          class="write-btn"
          @click="goWrite"
        >
          새 글 작성
        </el-button>
      </div>
    </div>

    <!-- 카드 쉐도우 컨테이너 -->
    <div class="board-card">
      <!-- Element UI 테이블 -->
      <el-table
        :data="boardList"
        @row-click="clickRow"
        class="custom-table"
        stripe
        highlight-current-row
      >
        <!-- 글번호 -->
        <el-table-column
          type="index"
          :index="indexMethod"
          label="#"
          width="80"
          align="center"
          class-name="col-index"
        ></el-table-column>

        <!-- 제목 -->
        <el-table-column prop="title" label="제목" min-width="200">
          <template slot-scope="scope">
            <span class="title-text">{{ scope.row.title }}</span>
          </template>
        </el-table-column>

        <!-- 내용 -->
        <el-table-column
          prop="content"
          label="내용"
          min-width="250"
          show-overflow-tooltip
        ></el-table-column>

        <!-- 작성자 -->
        <el-table-column
          prop="writer"
          label="작성자"
          width="160"
          align="center"
        >
          <template slot-scope="scope">
            <div class="writer-info">
              <span class="avatar-badge">{{
                scope.row.writer ? scope.row.writer.substring(0, 1) : "U"
              }}</span>
              <span class="writer-name">{{ scope.row.writer }}</span>
            </div>
          </template>
        </el-table-column>

        <!-- 등록일 -->
        <el-table-column
          prop="createdDate"
          label="등록일"
          width="150"
          align="center"
          :formatter="DateFormat"
        ></el-table-column>
      </el-table>

      <!-- 하단 페이징 영역 -->
      <div class="pagination-wrapper">
        <div class="total-count">
          Total <span>{{ totalElements }}</span> items
        </div>
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page="page"
          :page-size="size"
          :total="totalElements"
          @current-change="handlePageChange"
          class="custom-pagination"
        >
        </el-pagination>
      </div>
    </div>

    <board-detail ref="detailPopup" @reload="getBoardList()"></board-detail>
    <regist-board ref="registPopup" @reload="getBoardList"></regist-board>
  </div>
</template>

<script>
import axios from "axios";
import moment from "moment";
import BoardDetail from "./BoardDetail.vue";
import RegistBoard from "./RegistBoard.vue";

export default {
  name: "BoardList",
  components: { BoardDetail, RegistBoard },
  data() {
    return {
      boardList: [],
      page: 1,
      size: 10,
      totalElements: 0,
    };
  },
  created() {
    this.getBoardList();
  },
  methods: {
    indexMethod(index) {
      return this.totalElements - (this.page - 1) * this.size - index;
    },
    DateFormat(row, column, cellValue) {
      const date = cellValue || row.createdDate || row.regDate;
      return date ? moment(date).format("YYYY-MM-DD") : "-";
    },
    getBoardList() {
      axios
        .get("http://localhost:8080/api/boards", {
          params: {
            page: this.page,
            size: this.size,
          },
        })
        .then((response) => {
          if (response.data) {
            this.boardList = response.data.content || [];
            this.totalElements = response.data.totalElements || 0;
            console.log("페이징 정보 로드 완료:", response.data);
          }
        })
        .catch((error) => {
          console.error("데이터 로드 실패:", error);
        });
    },
    handlePageChange(newPage) {
      this.page = newPage;
      this.getBoardList();
    },
    clickRow(row) {
      // 행을 클릭하면 반드시 상세보기 팝업(detailPopup)을 호출하고, 해당 행(row) 데이터를 넘겨주어야 합니다.
      if (this.$refs.detailPopup) {
        this.$refs.detailPopup.getBoardDetail(row);
      }
    },
    // [추가] 글쓰기 클릭 이벤트 핸들러
    goWrite() {
      // 프로젝트 라우터 설계에 맞게 아래 주석 중 하나를 해제해서 사용해 보세요!
      this.$refs.registPopup.openPopup = true;
      // 1. Vue Router 페이지 이동 방식 적용 시:
      // this.$router.push("/write");

      // 2. 만약 글쓰기도 팝업창 모달(el-dialog) 형태를 사용 중이시라면:
      // this.$refs.writePopup.openPopup();
    },
  },
};
</script>

<style scoped>
/* 폰트 및 구역 기본 설정 */
.board-container {
  max-width: 1100px;
  margin: 50px auto;
  padding: 0 20px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    "Helvetica Neue", Arial, sans-serif;
  color: #333;
}

/* 헤더 영역 */
.board-header {
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: flex-end; /* 타이틀 하단 라인과 버튼 높이 정렬 */
}

.title-area .sub-title {
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 1.5px;
  color: #409eff;
  text-transform: uppercase;
  display: block;
  margin-bottom: 4px;
}

.title-area .main-title {
  font-size: 26px;
  font-weight: 700;
  color: #1e1e1e;
  margin: 0;
}

/* [추가] 글쓰기 버튼 스타일 고도화 */
.write-btn {
  border-radius: 20px !important; /* 둥글둥글하고 세련된 버튼 형태 */
  padding: 10px 22px !important;
  font-weight: 600 !important;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2) !important;
  transition: all 0.25s ease !important;
}

.write-btn:hover {
  transform: translateY(-2px); /* 호버 시 위로 스르륵 떠오르는 애니메이션 */
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.35) !important;
}

.write-btn:active {
  transform: translateY(0);
}

/* 카드 쉐도우 컨테이너 (부드러운 라운딩과 연한 그림자) */
.board-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(149, 157, 165, 0.12);
  border: 1px solid #ebeef5;
  overflow: hidden;
}

/* Element UI 테이블 커스텀 스타일 정의 */
.custom-table {
  width: 100%;
  font-size: 14px;
  border: none !important;
}

.custom-table >>> th {
  background-color: #fafbfe !important;
  color: #4a5568 !important;
  font-weight: 600 !important;
  padding: 16px 0 !important;
  border-bottom: 1.5px solid #edf2f7 !important;
}

.custom-table >>> td {
  padding: 14px 0 !important;
  border-bottom: 1px solid #edf2f7 !important;
}

/* 행 오버 스타일 (연한 블루 틴트 효과) */
.custom-table >>> .el-table__row {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.custom-table >>> .el-table__row:hover > td {
  background-color: rgba(64, 158, 255, 0.05) !important;
}

/* 인덱스 열 색상 처리 */
.custom-table >>> .col-index {
  color: #a0aec0;
  font-weight: 500;
}

/* 제목 호버 효과 */
.title-text {
  font-weight: 600;
  color: #2d3748;
  transition: color 0.15s ease;
}

.custom-table >>> .el-table__row:hover .title-text {
  color: #409eff;
}

/* 작성자 이니셜 아바타 장식 */
.writer-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.avatar-badge {
  width: 24px;
  height: 24px;
  background-color: #edf2f7;
  color: #4a5568;
  font-size: 11px;
  font-weight: 700;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  text-transform: uppercase;
}

.writer-name {
  color: #4a5568;
}

/* 페이징 네비게이션 영역 디자인 */
.pagination-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: #ffffff;
  border-top: 1px solid #edf2f7;
}

.total-count {
  font-size: 13px;
  color: #718096;
}

.total-count span {
  font-weight: 700;
  color: #409eff;
}

/* 페이징 버튼 라운드 처리 */
.custom-pagination >>> .btn-prev,
.custom-pagination >>> .btn-next,
.custom-pagination >>> .el-pager li {
  background-color: #f7fafc !important;
  border-radius: 6px !important;
  border: 1px solid #e2e8f0 !important;
  transition: all 0.2s ease;
}

.custom-pagination >>> .el-pager li.active {
  background-color: #409eff !important;
  color: #ffffff !important;
  border-color: #409eff !important;
  box-shadow: 0 4px 10px rgba(64, 158, 255, 0.25);
}
</style>
