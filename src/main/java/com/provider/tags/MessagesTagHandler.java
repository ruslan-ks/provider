package com.provider.tags;

import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.result.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;

public class MessagesTagHandler extends SimpleTagSupport {
    private static final String SUCCESS_MSG_ELEMENT = """
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>%s</strong>
                 <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            """;

    private static final String FAIL_MSG_ELEMENT = """
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>%s</strong>
                 <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            """;

    private static final Map<CommandResult.MessageType, String> MSG_ELEMENTS_MAP = Map.of(
            CommandResult.MessageType.SUCCESS, SUCCESS_MSG_ELEMENT,
            CommandResult.MessageType.FAIL, FAIL_MSG_ELEMENT
    );


    @Override
    public void doTag() throws IOException {
        final var messages = getMessages();
        if (messages != null) {
            final JspWriter writer = getJspContext().getOut();
            writer.write("<div class='container-fluid my-sm-3'>");
            while (!messages.isEmpty()) {
                final Pair<CommandResult.MessageType, String> pair = messages.poll();
                final String elementString = MSG_ELEMENTS_MAP.get(pair.getKey());
                writer.write(String.format(elementString, pair.getRight()));
            }
            writer.write("</div>");
        }
    }

    private Queue<Pair<CommandResult.MessageType, String>> getMessages() {
        final var pageContext = (PageContext) getJspContext();
        final var request = (HttpServletRequest) pageContext.getRequest();
        final HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        final var messages = (Queue<Pair<CommandResult.MessageType, String>>)
                session.getAttribute(SessionAttributes.MESSAGES);
        return messages;
    }
}
