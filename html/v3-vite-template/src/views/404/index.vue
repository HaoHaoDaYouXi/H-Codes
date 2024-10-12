<template>
  <div class="content">
    <p class="title">页面不存在</p>
    <p class="explain">请确认您输入的跳转地址是否正确，或者单击下方的按钮返回主页。</p>
    <router-link class="el-icon-back" tag="a" to="/" replace>
      <span>返回首页</span>
    </router-link>
  </div>
</template>

<style lang="scss" scoped>
.content {
  position: absolute;
  left: 47%;
  top: 38%;
  transform: translate(-50%, -50%);
  z-index: 1;
  text-align: left;
}

.title {
  line-height: 1.391304;
  font-size: 46px;
  font-family:
    PingFang SC-Bold,
    PingFang SC;
  color: #333333;
}

.explain {
  margin-top: 16px;
  line-height: 1.285714;
  font-size: 32px;
  font-family:
    PingFang SC-Bold,
    PingFang SC;
  color: #666666;
}

.el-icon-back {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 160px;
  height: 60px;
  margin-top: 32px;
  margin-left: 90%;
  color: #fff;
  background: #2975f9;
  font-size: 24px;

  span {
    margin-left: 6px;
  }
}
</style>
