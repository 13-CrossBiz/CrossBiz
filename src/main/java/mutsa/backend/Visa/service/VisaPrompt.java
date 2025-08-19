package mutsa.backend.Visa.service;

import mutsa.backend.Visa.dto.request.visa.BasicInfo;
import mutsa.backend.Visa.dto.request.visa.WithVisaInfo;
import mutsa.backend.Visa.dto.request.visa.WithoutVisaInfo;
import org.springframework.stereotype.Service;

@Service
public class VisaPrompt {
    public String withVisaPromt(BasicInfo basic, WithVisaInfo info) {
        return String.format(
                """
                        다음은 외국인의 정보입니다.
                        이 사람은 현재 한국에서 발급받은 비자를 보유하고 있으며, 한국에서 사업을 운영하거나 운영할 예정입니다.
                        이 정보를 바탕으로, 현재 비자외에 발급받을 수 있는 비자 3가지를 추천해주세요.
                        각각의 비자를 추천하는 이유를 3가지 알려주고 비자 취득 시 주의해야할 사항을 알려주세요.
                        그리고 해당 비자에 대한 자세한 설명(목적, 대상, 신청 자격, 필요 서류, 혜택의 내용을 포함해서)을 알려주세요.
                        그리고 향후 고려할 수 있는 비자 변경/연장/대체 옵션들 4가지를 추천해 주세요.
                        마지막으로 위 정보를 아래와 같은 JSON 형식으로 변환해서 JSON 형태로만 반환해서 알려주세요.
                        [JSON 형식]
                        {
                          "summary": "요약 설명",
                          "recommendedVisas": [
                            {
                              "name": "비자 이름",
                              "reason": "비자를 추천하는 이유",
                              "purpose": "비자의 목적",
                              "target": "대상",
                              "qualification": "신청 자격",
                              "requiredDocuments": ["서류1", "서류2", "서류3"],
                              "benefits": ["혜택1", "혜택2"],
                              "cautions": ["주의사항1", "주의사항2"]
                            }
                          ],
                          "visaCautions": ["주의사항1", "주의사항2"],
                          "alternativeOptions": [
                            {
                              "name": "비자 이름",
                              "description": "대체 가능 비자 설명",
                              "qualification": "자격 요건"
                            }
                          ]
                        }
                                
                        [기본 정보]
                        - 국적: %s
                        - 발급받은 비자 종류: %s
                        - 체류 자격: %s
                        - 예상 체류 기간: %d개월
                        - 학위: %s
                        - 관련 근무 경력: %d년
                        - 한국어 능력: %s
                                
                        [비자 정보]
                        - 비자 발급일: %s
                        - 비자 만료일: %s
                                
                        [사업 정보]
                        - 사업자 등록 번호: %s
                        - 연매출: %,d원
                        - 고용 인원: %d명
                                
                       
                        """,
                basic.getNationality(),
                info.getVisaType(),
                basic.getStatus(),
                basic.getEstimatePeriod(),
                basic.getDegree(),
                basic.getWorkExperience(),
                basic.getKoreanLevel(),
                info.getIssuedDate(),
                info.getExpiryDate(),
                info.getBusinessRegNumber(),
                info.getAnnualRevenue(),
                info.getEmployeeCount()
        );
    }
    public String withoutVisaPromt(BasicInfo basic, WithoutVisaInfo info) {
        return String.format(
                """
                        다음은 외국인의 정보입니다. 이 사람이 한국에서 사업을 하려 합니다.
                        이 정보들을 바탕으로, 어떤 종류의 비자가 적합할지 3가지를 추천해 주세요.
                        
                        각각의 비자를 추천하는 이유를 3가지 알려주고 비자 취득 시 주의해야할 사항을 알려주세요.
                        그리고 해당 비자에 대한 자세한 설명(목적, 대상, 신청 자격, 필요 서류, 혜택의 내용을 포함해서)을 알려주세요.
                        그리고 이외의 어떠한 조건을 충족하면 다른 비자도 충족할 수 있을지 취득 가능 비자 4가지도 추천해주세요
                        마지막으로 위 정보를 아래와 같은 JSON 형식으로 변환해서 JSON 형태로만 반환해서 알려주세요.
                        
                        [JSON 형식]
                        {
                          "summary": "요약 설명",
                          "recommendedVisas": [
                            {
                              "name": "비자 이름",
                              "reason": "비자를 추천하는 이유",
                              "purpose": "비자의 목적",
                              "target": "대상",
                              "qualification": "신청 자격",
                              "requiredDocuments": ["서류1", "서류2", "서류3"],
                              "benefits": ["혜택1", "혜택2"],
                              "cautions": ["주의사항1", "주의사항2"]
                            }
                          ],
                          "visaCautions": ["주의사항1", "주의사항2"],
                          "alternativeOptions": [
                            {
                              "name": "비자 이름",
                              "description": "대체 가능 비자 설명",
                              "qualification": "자격 요건"
                            }
                          ]
                        }
                        
                        [기본 정보]
                        - 나이: %d
                        - 국적: %s
                        - 체류 자격: %s
                        - 학력: %s
                        - 한국어 수준: %s
                        - 직업 상태: %s
                        - 사업 분야: %s
                        - 예상 체류 기간: %d개월
                        - 관련 업무 경력: %d년

                        [사업 계획 정보]
                        - 경력 요약: %s
                        - 예상 사업장 형태: %s
                        - 특허/지식재산권 보유 여부: %s
                        - 영업 준비금: %,d원
                        - 오아시스 점수: %.1f
                        """,
                basic.getAge(),
                basic.getNationality(),
                basic.getStatus(),
                basic.getDegree(),
                basic.getKoreanLevel(),
                basic.getBizStatus(),
                basic.getBizCategory(),
                basic.getEstimatePeriod(),
                basic.getWorkExperience(),
                info.getCareer(),
                info.getExpectedBusinessType(),
                info.getHasIntellectualProperty() ? "있음" : "없음",
                info.getBusinessFund(),
                info.getOasisScore()
        );
    }
}
