<template>
  <div id="app">
    <board-list ref="list"></board-list>
  </div>
</template>

<script>
import BoardList from "./components/BoardList.vue";

export default {
  name: "App",
  components: {
    BoardList,
  },
  methods: {
    reload() {
      this.$refs.list.getBoardList();
    },
  },
};
</script>
<style></style>
