document.addEventListener('DOMContentLoaded', () => {
    const tabs = document.querySelectorAll('[role="tab"]');
    const tabPanels = document.querySelectorAll('[role="tabpanel"]');
    const synth = window.speechSynthesis; // 웹 스피치 API 초기화

    tabs.forEach(tab => {
        tab.addEventListener('keydown', function(event) {
            console.log(`Key pressed: ${event.key}`); // 이벤트 확인용 로그 추가

            if (event.key === 'Tab') {
                event.preventDefault();
                const index = Array.from(tabs).indexOf(tab);
                let nextTab;
                if (event.shiftKey) {
                    nextTab = tabs[index - 1] || tabs[tabs.length - 1];
                } else {
                    nextTab = tabs[index + 1] || tabs[0];
                }
                nextTab.focus();
            }

            if (event.key === 'a') { // 'a' 키 감지
                event.preventDefault(); // 기본 동작 방지 (스크롤 등)
                console.log('A key pressed'); // 'a' 키 인식 확인용 로그 추가
                const textToRead = tab.textContent.trim(); // 탭의 텍스트 가져오기
                const utterance = new SpeechSynthesisUtterance(textToRead); // 음성 발화 객체 생성
                synth.speak(utterance); // 음성 발화 시작
            }

            if (event.key === 'Enter') {
                event.preventDefault(); // 기본 동작 방지 (폼 제출 등)
                tab.click(); // 탭 클릭 처리
            }
        });
    });

    // Initialize first tab as selected
    tabs[0].setAttribute('aria-selected', 'true');
    tabPanels[0].hidden = false;
});
