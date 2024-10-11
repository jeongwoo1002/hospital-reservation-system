package hello.login.domain.validator;

import hello.login.domain.dto.BoardForm;
import hello.login.domain.model.Board;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class BoardValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        BoardForm board = (BoardForm) obj;

        if (StringUtils.isEmpty(board.getContent())) {
            errors.rejectValue("content", "err", "내용을 입력해 주세요");
        }
    }
}
