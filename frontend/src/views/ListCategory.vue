<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useRouter } from 'vue-router'
import ListCategory from '../components/ListCateComponent.vue';

const categories = ref([])
const getCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/categories`)
  if (res.status === 200) {
    categories.value = await res.json()
    console.log(categories.value)
  } else console.log('error, cannot get data')
}
onBeforeMount(async () => {
  await getCategory()
})


</script>
<template>
<div>
  <ListCategory :categories="categories"></ListCategory>
</div>

</template>

<style scoped>
/* img {
  padding: 5%;
  margin: 5%;
  border: 0.2em solid white;
  border-radius: 4%;
} */

/* img:hover {
  border: 0.2em solid white;
  border-radius: 4%;
  background-color: rgba(255, 255, 255, 0.3);
} */

.category-box {
  background-color: rgba(255, 255, 255, 0.5);
  width: 150%;
  height: 12em;
  color: #383838;
  margin-left: 20%;
  margin-bottom: 3%;
  padding: 3%;
}

#duration {
  background-color: #eee385;
}

.edit {
  width: 79.5%;
  margin-bottom: 3%;
}

.Btn {
  text-align: center;
}
</style>
