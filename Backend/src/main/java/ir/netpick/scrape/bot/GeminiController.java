package ir.netpick.scrape.bot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import ir.netpick.scrape.scrapper.ApiCaller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/ai")
public class GeminiController {
    private final GenerateTextFromTextInput gemini;
    private final Gson gson;
    private final ApiCaller apiCaller;

    public GeminiController(GenerateTextFromTextInput gemini, Gson gson, ApiCaller apiCaller) {
        this.gemini = gemini;
        this.gson = gson;
        this.apiCaller = apiCaller;
    }

    @GetMapping("text")
    public ResponseEntity<String> text(@RequestBody String text) {
        return ResponseEntity.ok().body(gson.toJson(gemini.generateText(text)));
    }

    @GetMapping("api")
    public void api() {
        apiCaller.caller();
    }

}
