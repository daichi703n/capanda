// package com.daichi703n.capanda.domain;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;

// import com.github.seratch.jslack.Slack;
// import com.github.seratch.jslack.api.model.Attachment;
// import com.github.seratch.jslack.api.model.Field;
// import com.github.seratch.jslack.api.webhook.Payload;
// import com.github.seratch.jslack.api.webhook.WebhookResponse;

// import org.springframework.stereotype.Service;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Service
// public class SlackService {

//     public WebhookResponse hello() throws IOException {
//         String url = System.getenv("SLACK_WEBHOOK_URL");
        
//         Payload payload = Payload.builder()
//         .text("Hello World!")
//         .build();
        
//         Slack slack = Slack.getInstance();
//         WebhookResponse response = slack.send(url, payload);
//         // response.code, response.message, response.body

//         return response;
//     }

//     public WebhookResponse send(List<CapandaInfo> instances) throws IOException {
//         // TODO: load from application.yml
//         String url = System.getenv("SLACK_WEBHOOK_URL");
//         String username = System.getenv("SLACK_USERNAME");
//         String channel = System.getenv("SLACK_CHANNEL");
//         String iconEmoji = System.getenv("SLACK_ICON_EMOJI");
//         String capandaUrl = System.getenv("CAPANDA_URL");

//         List<Attachment> attachments = new ArrayList<Attachment>();

//         instances.forEach(i -> {
//             List<Field> fields = new ArrayList<>();
//             fields.add(Field.builder()
//                 .title("Deployment")
//                 .value(i.getDeployment())
//                 .valueShortEnough(true)
//                 .build());
//             fields.add(Field.builder()
//                 .title("Name")
//                 .value(i.getName())
//                 .valueShortEnough(true)
//                 .build());
//             fields.add(Field.builder()
//                 .title("Type")
//                 .value(i.getType())
//                 .valueShortEnough(true)
//                 .build());
//             fields.add(Field.builder()
//                 .title("AvailabilityZone")
//                 .value(i.getAvailabilityZone())
//                 .valueShortEnough(true)
//                 .build());
//             fields.add(Field.builder()
//                 .title("Lifecycle")
//                 .value(i.getLifecycle())
//                 .valueShortEnough(true)
//                 .build());
//             fields.add(Field.builder()
//                 .title("LaunchTime")
//                 .value(i.getLaunchTime())
//                 .valueShortEnough(true)
//                 .build());

//             Attachment attachment = Attachment.builder()
//                 .color("warning")
//                 .fields(fields)
//                 .build();
//             attachments.add(attachment);
//         });

//         if (attachments.isEmpty()){
//             log.info("No Illegal Instances.");
//             return null;
//         }else{
//             Payload payload = Payload.builder()
//                 .text("<"+capandaUrl+"/instances|Capanda detects warnings>")
//                 .username(username)
//                 .channel(channel)
//                 .iconEmoji(iconEmoji)
//                 .attachments(attachments)
//                 .build();
            
//             log.info("POST to Slack: {}",payload);
//             Slack slack = Slack.getInstance();
//             WebhookResponse response = slack.send(url, payload);
//             // response.code, response.message, response.body

//             log.info("Response from Slack: {}",response);
//             return response;
//         }
//     }
// }
