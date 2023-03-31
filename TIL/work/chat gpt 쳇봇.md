
```python
import openai

#pip install openai

openai.api_key = "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"

  

"""

시스템 메시지: ‘You are a helpful assistant.’와 같은 메시지로 도우미에게

    지시할 수 있습니다. 시스템이 챗봇에게 일종의 역할을 부여한다고 볼 수 있습니다.

사용자 메시지: 도우미에게 직접 전달하는 내용입니다.

도우미 메시지: 이전에 응답했던 결과를 저장해 대화의 흐름을 유지할 수 있도록 설정할 수 있습니다.

"""

messages = []

while True:

    user_content = input("user : ")

    messages.append({"role": "user", "content": f"{user_content}"})

  

    completion = openai.ChatCompletion.create(model="gpt-3.5-turbo", messages=messages)

  

    print(completion)

    assistant_content = completion.choices[0].message["content"].strip()

    messages.append({"role": "assistant", "content": f"{assistant_content}"})

  

    print(f"GPT : {assistant_content}")

  
  
  

"""

{

  "choices": [

    {

      "finish_reason": "stop",

      "index": 0,

      "message": {

        "content": "The iPhone 1 was released on June 29, 2007,

            and it was priced at $499 for the 4GB model and $599 for the 8GB model.",

        "role": "assistant"

      }

    }

  ],

  "created": 1680065687,

  "id": "chatcmpl-6zI7bACzwqzs6Hq7e0vwXvVAZsjYd",

  "model": "gpt-3.5-turbo-0301",

  "object": "chat.completion",

  "usage": {

    "completion_tokens": 38,

    "prompt_tokens": 16,

    "total_tokens": 54

  }

}

"""
```

