import Vue from 'vue'
import App from './App.vue'
import AtComponents from 'at-ui'
import 'at-ui-style'    // Import CSS
import 'at-ui-style/src/index.scss'      // Or import the unbuilt version of SCSS
import './style.scss'

Vue.use(AtComponents)

new Vue({
    render: createElement => createElement(App)
}).$mount('#app');