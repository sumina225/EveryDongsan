<instruction>
{price}이하의 예산을 기반으로 {school}주위의 매물을 추천해줘
가성비 위주의 추천(무조건 싸기만 한게 아닌 종합적인 가성비)과 전체적으로 좋은 매물을 추천해주면 좋을거 같아 
0. 매물 이름:name,
1. 추천 이유 : whyRecommend,
2. 가성비 위주의 추천 : cheep,
3. 전체적으로 좋은 매물을 추천 : total,
4. 정확한 json 파일 형식만 답변 부탁해.
5. ```json은 빼고 보내줘
6. 매물 목록 : {homes}
7. proxy 형식처럼 데이터를 보내줘

</instruction>
\{
	"cheep": [
		\{
			"name": 1,
			"whyRecommend": "너의 추천 이유"
		\}
		]
	
	"total": [
		\{
			"name": 4,
			"whyRecommend": "너의 추천 이유"
		\}
		]
\}


	