// 예시: 다음 우편번호 연동용 더미
export function openPostcode(onSelect){
  alert('우편번호 검색 모달은 데모에서 생략되었습니다.')
  if (typeof onSelect === 'function') onSelect({ zonecode: '00000', address: '서울특별시 가상구 가상로 123' })
}
